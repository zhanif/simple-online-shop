package com.zein.online_shop.service.impl;

import com.zein.online_shop.dto.request.ItemRequest;
import com.zein.online_shop.dto.response.ItemResponse;
import com.zein.online_shop.dto.response.ListResponse;
import com.zein.online_shop.model.Item;
import com.zein.online_shop.repository.ItemRepository;
import com.zein.online_shop.service.ItemService;
import com.zein.online_shop.utility.UniqueCodeGenerator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ItemServiceImpl extends BaseServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;

    @Override
    public ListResponse getAll(int page, int size, List<String> sort) {
        Page<Item> items = itemRepository.findAllBy(PageRequest.of(page, size, Sort.by(getSortOrder(sort))));
        List<ItemResponse> responses = Arrays.asList(modelMapper.map(items.toList(), ItemResponse[].class));

        return new ListResponse(responses, getPageMetadata(items));
    }

    @Override
    public ItemResponse get(Integer id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item (ID: " + id + ") not found"));

        return modelMapper.map(item, ItemResponse.class);
    }

    @Override
    public ItemResponse create(ItemRequest request) {
        Item item = modelMapper.map(request, Item.class);

        int maxLoop = 5;
        while(maxLoop > 0) {
            // generating custom 6-digit code
            String code = UniqueCodeGenerator.generate(6);

            if (!itemRepository.existsByCode(code)) {
                item.setCode(code);
                break;
            }

            maxLoop--;
        }

        item = itemRepository.save(item);
        return modelMapper.map(item, ItemResponse.class);
    }

    @Override
    public ItemResponse update(Integer id, ItemRequest request) {
        Item item = itemRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Item (ID: " + id + ") not found"));

        item.setName(request.getName());
        item.setPrice(request.getPrice());
        item.setStock(request.getStock());
        item.setLastReStock(request.getLastReStock());
        item.setIsAvailable(request.getIsAvailable());

        item = itemRepository.save(item);
        return modelMapper.map(item, ItemResponse.class);
    }

    @Override
    public void delete(Integer id) {
        itemRepository.deleteById(id);
    }
}
