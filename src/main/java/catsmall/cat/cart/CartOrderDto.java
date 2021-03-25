package catsmall.cat.cart;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CartOrderDto {
    private Long itemId;
    private Integer count;
    private List<CartOrderDto> cartOrderDtoList = new ArrayList<>();
    private boolean hasErrors;
}
