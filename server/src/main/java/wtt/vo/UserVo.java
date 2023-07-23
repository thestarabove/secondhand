package wtt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserVo {
    private Integer userId;
    private String userName;
    private String userPhone;
    private String account;
    private String address;
    private  Integer role;
    private String img;
    private BigDecimal money;
}
