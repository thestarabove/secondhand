<template>
  <Menu />
  <div class="chat-page">
    <div class="chat-container">
      <!-- 左侧：会话列表 -->
      <div class="session-panel">
        <div class="panel-header">消息</div>
        <div v-if="sessions.length === 0" class="empty">暂无会话</div>
        <div
          v-for="item in sessions"
          :key="item.sessionId"
          class="session-item"
          :class="{ active: activeSession && activeSession.sessionId === item.sessionId }"
          @click="openSession(item)"
        >
          <div class="session-info">
            <div class="session-name">
              <span class="peer-name">{{ item.peerUserName || '用户' + item.peerUserId }}</span>
              <el-badge
                v-if="item.unreadCount > 0"
                :value="item.unreadCount"
                class="unread-badge"
              />
            </div>
            <div class="session-preview">{{ item.lastMessage || '暂无消息' }}</div>
          </div>
          <div class="session-time">{{ formatTime(item.lastMessageTime) }}</div>
        </div>
      </div>
      <!-- 右侧：聊天窗口 -->
      <div class="message-panel">
        <template v-if="activeSession">
          <div class="chat-header">
            <span class="peer-name">{{ activeSession.peerUserName || '用户' + activeSession.peerUserId }}</span>
            <span v-if="activeSession.goodTitle" class="good-title">（商品：{{ activeSession.goodTitle }}）</span>
          </div>
          <div ref="messageList" class="message-list">
            <div
              v-for="msg in messages"
              :key="msg.messageId"
              class="message-row"
              :class="{ mine: msg.senderId === currentUserId }"
            >
              <div class="bubble">{{ msg.content }}</div>
              <div class="message-time">{{ formatTime(msg.createTime) }}</div>
            </div>
          </div>
          <div class="chat-input">
            <el-input
              v-model="inputText"
              type="textarea"
              :rows="3"
              placeholder="输入消息，Ctrl+Enter 发送"
              @keydown.ctrl.enter.prevent="sendMessage"
            />
            <div class="send-bar">
              <span v-if="wsStatus === 'offline'" class="ws-tip">实时连接已断开，消息将通过刷新获取</span>
              <el-button type="primary" @click="sendMessage">发送</el-button>
            </div>
          </div>
        </template>
        <div v-else class="empty full">选择一个会话开始聊天</div>
      </div>
    </div>
  </div>
</template>

<script>
import Menu from '../components/Menu.vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'

export default {
  name: 'Chat',
  components: {
    Menu
  },
  data() {
    return {
      sessions: [],
      activeSession: null,
      messages: [],
      inputText: '',
      currentUserId: null,
      ws: null,
      wsTimer: null,
      wsStatus: 'offline'
    }
  },
  created() {
    const uid = this.$cookies.get('userId')
    this.currentUserId = uid ? Number(uid) : null
  },
  mounted() {
    this.loadSessions()
      .then(() => {
        const sessionId = this.$route.query.sessionId
        if (sessionId) {
          const target = this.sessions.find((s) => String(s.sessionId) === String(sessionId))
          if (target) {
            this.openSession(target)
          }
        }
      })
    this.connectWebSocket()
  },
  beforeUnmount() {
    if (this.ws) {
      this.ws.onclose = null
      this.ws.close()
    }
    if (this.wsTimer) {
      clearTimeout(this.wsTimer)
    }
  },
  methods: {
    loadSessions() {
      return axios.get('/chat/sessionList').then((res) => {
        this.sessions = res.data.data || []
        if (this.activeSession) {
          const current = this.sessions.find(
            (s) => s.sessionId === this.activeSession.sessionId
          )
          if (current) {
            this.activeSession = current
          }
        }
      })
    },
    openSession(session) {
      this.activeSession = session
      session.unreadCount = 0
      axios
        .get('/chat/messages', {
          params: { sessionId: session.sessionId }
        })
        .then((res) => {
          this.messages = res.data.data || []
          this.scrollToBottom()
        })
        .catch(() => {
          this.messages = []
        })
    },
    sendMessage() {
      const content = (this.inputText || '').trim()
      if (!content) {
        return
      }
      if (!this.activeSession) {
        return
      }
      axios
        .post('/chat/send', {
          sessionId: this.activeSession.sessionId,
          receiverId: this.activeSession.peerUserId,
          content
        })
        .then((res) => {
          const msg = res.data.data
          if (msg) {
            this.messages.push(msg)
            this.scrollToBottom()
          }
          this.inputText = ''
        })
        .catch(() => {
          ElMessage({
            message: '发送失败，请确认已登录',
            type: 'warning'
          })
        })
    },
    connectWebSocket() {
      const token = this.$cookies.get('satoken')
      const query = token ? '?token=' + encodeURIComponent(token) : ''
      const protocol = location.protocol === 'https:' ? 'wss' : 'ws'
      try {
        this.ws = new WebSocket(`${protocol}://${location.host}/ws/chat${query}`)
      } catch (e) {
        this.scheduleReconnect()
        return
      }
      this.ws.onopen = () => {
        this.wsStatus = 'online'
      }
      this.ws.onmessage = (event) => {
        try {
          const payload = JSON.parse(event.data)
          if (payload.type === 'CHAT') {
            this.onMessage(payload.data)
          }
        } catch (e) {
          console.error(e)
        }
      }
      this.ws.onclose = () => {
        this.wsStatus = 'offline'
        this.scheduleReconnect()
      }
      this.ws.onerror = () => {
        if (this.ws) {
          this.ws.close()
        }
      }
    },
    scheduleReconnect() {
      if (this.wsTimer) {
        clearTimeout(this.wsTimer)
      }
      this.wsTimer = setTimeout(() => this.connectWebSocket(), 3000)
    },
    onMessage(data) {
      const sessionId = data.sessionId
      if (this.activeSession && String(this.activeSession.sessionId) === String(sessionId)) {
        this.messages.push(data)
        this.scrollToBottom()
        return
      }
      const item = this.sessions.find((s) => String(s.sessionId) === String(sessionId))
      if (item) {
        item.lastMessage = data.content
        item.lastMessageTime = data.createTime
        item.unreadCount = (item.unreadCount || 0) + 1
        this.sessions.sort((a, b) => {
          const at = a.lastMessageTime ? new Date(a.lastMessageTime).getTime() : 0
          const bt = b.lastMessageTime ? new Date(b.lastMessageTime).getTime() : 0
          return bt - at
        })
      } else {
        this.loadSessions()
      }
    },
    scrollToBottom() {
      this.$nextTick(() => {
        const el = this.$refs.messageList
        if (el) {
          el.scrollTop = el.scrollHeight
        }
      })
    },
    formatTime(time) {
      if (!time) {
        return ''
      }
      const d = new Date(time)
      const pad = (n) => (n < 10 ? '0' + n : '' + n)
      return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}`
    }
  }
}
</script>

<style scoped>
.chat-page {
  height: calc(100vh - 60px);
  padding: 20px;
  box-sizing: border-box;
  background-color: #f0f2f5;
}

.chat-container {
  display: flex;
  height: 100%;
  max-width: 1100px;
  margin: 0 auto;
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.session-panel {
  width: 300px;
  border-right: 1px solid #e4e7ed;
  overflow-y: auto;
}

.panel-header {
  height: 50px;
  line-height: 50px;
  padding: 0 16px;
  font-size: 16px;
  font-weight: 600;
  border-bottom: 1px solid #e4e7ed;
}

.session-item {
  display: flex;
  justify-content: space-between;
  padding: 14px 16px;
  cursor: pointer;
  border-bottom: 1px solid #f5f5f5;
}

.session-item:hover {
  background: #f5f7fa;
}

.session-item.active {
  background: #ecf5ff;
}

.session-info {
  min-width: 0;
  flex: 1;
}

.session-name {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  font-weight: 500;
}

.peer-name {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.unread-badge {
  margin-left: 8px;
}

.session-preview {
  margin-top: 6px;
  font-size: 12px;
  color: #909399;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.session-time {
  margin-left: 8px;
  font-size: 12px;
  color: #c0c4cc;
  white-space: nowrap;
}

.message-panel {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-width: 0;
}

.chat-header {
  height: 50px;
  line-height: 50px;
  padding: 0 16px;
  border-bottom: 1px solid #e4e7ed;
  font-size: 15px;
  font-weight: 500;
}

.good-title {
  font-size: 12px;
  color: #909399;
  font-weight: 400;
}

.message-list {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  background: #f7f8fa;
}

.message-row {
  margin-bottom: 14px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.message-row.mine {
  align-items: flex-end;
}

.bubble {
  max-width: 70%;
  padding: 10px 14px;
  border-radius: 8px;
  background: #fff;
  font-size: 14px;
  line-height: 1.5;
  word-break: break-word;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
}

.message-row.mine .bubble {
  background: #409eff;
  color: #fff;
}

.message-time {
  margin-top: 4px;
  font-size: 11px;
  color: #c0c4cc;
}

.chat-input {
  padding: 12px 16px;
  border-top: 1px solid #e4e7ed;
}

.send-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 8px;
}

.ws-tip {
  font-size: 12px;
  color: #e6a23c;
}

.empty {
  padding: 40px 0;
  text-align: center;
  color: #909399;
  font-size: 14px;
}

.empty.full {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
