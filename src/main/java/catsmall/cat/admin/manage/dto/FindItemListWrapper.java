package catsmall.cat.admin.manage.dto;

import lombok.Data;

import java.util.List;

@Data
public class FindItemListWrapper {
    private List<FindItemList> findItemList;
    private boolean hasNext;
}
