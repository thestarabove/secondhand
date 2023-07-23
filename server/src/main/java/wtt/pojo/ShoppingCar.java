package wtt.pojo;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.lang.annotation.ElementType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ShoppingCar implements Serializable {
    private static final long serialVerSionUID=7123687508750315545L;
    private String carId;
    private Integer userId;
    private Integer secondHandId;
    private Integer GoodAmount;

}
