package catsmall.cat.admin;

import catsmall.cat.admin.manage.AdminManageMemberService;
import catsmall.cat.admin.manage.dto.FindItemList;
import catsmall.cat.admin.manage.dto.FindItemListWrapper;
import catsmall.cat.admin.repository.AdminCategoryRepository;
import catsmall.cat.admin.repository.AdminItemRepository;
import catsmall.cat.admin.service.AdminCategoryService;
import catsmall.cat.admin.service.AdminItemService;
import catsmall.cat.config.AppProperties;
import catsmall.cat.entity.Category;
import catsmall.cat.entity.ItemCategory;
import catsmall.cat.entity.dto.ItemTypeDto;
import catsmall.cat.entity.item.Item;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/api")
public class AdminRestController {
    private final AdminItemService adminItemService;
    private final AdminCategoryService adminCategoryService;
    private final AdminCategoryRepository adminCategoryRepository;
    private final AdminManageMemberService adminManageService;
    private final AppProperties appProperties;
    private final AdminItemRepository adminItemRepository;

    @PostMapping("/image-upload")
    public ImageUpload imageUpload(@RequestBody MultipartFile file) throws IOException {
        // TODO 이미지 업로드 경로 배포할때는 리눅스용으로 바꿀것
        String imgPrefix = appProperties.getImgPrefix();
        File directory = new File(imgPrefix + "image/");
        if(!directory.exists()){
            directory.mkdirs();
        }
        String extension = FilenameUtils.getExtension(file.getOriginalFilename()); // 파일명에서 확장자 추출
        String fileName = UUID.randomUUID().toString() + "_" + LocalDateTime.now() + "." + extension;
        File fileSetting = new File(directory, fileName);
        file.transferTo(fileSetting);

        String url = appProperties.getHost() + "/upload/images/" + fileName;
        ImageUpload imageUpload = new ImageUpload();
        imageUpload.setUrl(url);
        return imageUpload;
    }

    @PostMapping("/getSubCategories")
    public RestWrapper getSubCategories(@RequestBody ItemTypeDto itemTypeDto){
        Category topCategory = adminCategoryRepository.findChildrenFetchByParentName(itemTypeDto.getCategoryName());
        RestWrapper restWrapper = new RestWrapper(topCategory.getChildren());
        return restWrapper;
    }

    @PostMapping("/modifyType")
    public String modifyType(@RequestBody ItemTypeDto itemTypeDto){
        adminItemService.modifyType(itemTypeDto);
        return "success";
    }

    @DeleteMapping("/deleteType")
    public String deleteType(@RequestBody ItemTypeDto itemTypeDto){
        System.out.println(itemTypeDto.getCategoryName());
        System.out.println(itemTypeDto.getType());
        adminItemService.deleteType(itemTypeDto);
        return "success";
    }

    @PostMapping("/block-user")
    public ResponseEntity block_user(@RequestBody BlockUserDto blockUserDto){
        adminManageService.blockUser(blockUserDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unblock-user")
    public ResponseEntity unBlock_user(@RequestBody BlockUserDto blockUserDto){
        adminManageService.unBlockUser(blockUserDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/find-item-list")
    public FindItemListWrapper find_itemList(@RequestBody FindItemList findItemList, Pageable pageable){
        Page<ItemCategory> originList = adminItemRepository.findItemPagingFetchByCategory(findItemList.getCategory(), pageable);
        List<ItemCategory> tempItemCategory = originList.getContent();
        List<FindItemList> findItemLists = new ArrayList<>();
            for (ItemCategory itemCategory : tempItemCategory) {
                Item item = itemCategory.getItem();
                findItemLists
                        .add(new FindItemList(item.getId(), item.getName(), item.getThumbnailName(),
                                item.getQuantity(), item.getPrice(), item.isEvent(),
                                item.getEventPrice(), item.getDiscount(), item.getItemStatus()));
            }
        FindItemListWrapper findItemListWrapper = new FindItemListWrapper();
        if(findItemLists.isEmpty()){
            findItemListWrapper.setFindItemList(Collections.EMPTY_LIST);
            return findItemListWrapper;
        }
        findItemListWrapper.setFindItemList(findItemLists);
        findItemListWrapper.setHasNext(originList.hasNext());
        return findItemListWrapper;
    }

    @Data
    static class RestWrapper{
        private List<Category> children;

        public RestWrapper(List<Category> children) {
            this.children = children;
        }
    }
}
