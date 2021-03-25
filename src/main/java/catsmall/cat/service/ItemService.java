package catsmall.cat.service;

import catsmall.cat.entity.dto.item.ItemDto;
import catsmall.cat.entity.dto.ItemTypeDto;
import catsmall.cat.entity.dto.item.ItemListDto;
import catsmall.cat.entity.item.CatFood;
import catsmall.cat.entity.item.CatToilet;
import catsmall.cat.entity.item.CatTower;
import catsmall.cat.entity.item.Item;
import catsmall.cat.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    public List<Item> findAll(){
        return itemRepository.findAll();
    }

    public List<ItemTypeDto> findItemTypesByCategory(String category){
        return itemRepository.findItemTypesByCategory(category);
    }

    public Item findById(Long id){
        return itemRepository.findById(id).get();
    }

    public Page<ItemListDto> findAllByType(String category, String type, Pageable pageable, boolean isEvent){
        return itemRepository.findAllCategoryItemByType(category, type, pageable, false);
    }

}
