package wtt.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

@TableName("tb_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class User {
    @TableId(value = "user_id",type = IdType.AUTO)
    private Integer userId;
    private String userName;
    private String userPhone;
    private String account;

    private String password;
    private BigDecimal money;
   private String address;
   private  Integer role;
   private String img;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId) && Objects.equals(userName, user.userName) && Objects.equals(userPhone, user.userPhone) && Objects.equals(account, user.account) && Objects.equals(password, user.password) && Objects.equals(money, user.money) && Objects.equals(address, user.address) && Objects.equals(role, user.role) && Objects.equals(img, user.img);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, userName, userPhone, account, password, money, address, role, img);
    }
}
