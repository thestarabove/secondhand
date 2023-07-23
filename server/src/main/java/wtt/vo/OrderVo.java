package wtt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderVo {
    private  Integer orderId;
    private String userName;
    private Integer secondHandId;
    private Date createTime;
    private Date updateTime;
    private String goodName;
    private List<String> cartImg;
    private String address;
    private String total;
    private String num;
}
