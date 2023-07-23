package wtt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pager<T> {
    private int page;//分页起始页
    private int size;//每页记录数
    private List<T> data;//返回的记录集合
    private Long total;//总记录条数

}
