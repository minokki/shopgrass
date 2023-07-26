package com.grassshop.service;

import com.grassshop.constant.ItemStatus;
import com.grassshop.domain.Item;
import com.grassshop.domain.ItemImg;
import com.grassshop.dto.ItemFormDto;
import com.grassshop.dto.SignUpForm;
import com.grassshop.repository.ItemImgRepository;
import com.grassshop.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemServiceTest {
    @Autowired
    ItemService itemService;

    @Autowired
    AccountService accountService;

    @Autowired
    ItemImgRepository itemImgRepository;

    @Autowired
    ItemRepository itemRepository;

    List<MultipartFile> createMultipartFiles() {
        List<MultipartFile> multipartFileList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            String path = "C:/shop/item/";
            String imageName = "image" + i + ".jpg";
            MockMultipartFile multipartFile = new MockMultipartFile(path, imageName, "image/jpg", new byte[]{1, 2, 3, 4});
            multipartFileList.add(multipartFile);
        }
        return multipartFileList;
    }

    @Test
    @DisplayName("상품 등록 테스트")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void saveItem() throws Exception {


        ItemFormDto itemFormDto = new ItemFormDto();
        itemFormDto.setItemNm("테스트상품");
        itemFormDto.setItemStatus(ItemStatus.SELL);
        itemFormDto.setItemDetail("상품설명");
        itemFormDto.setPrice(10000);
        itemFormDto.setStockNumber(1000);

        List<MultipartFile> multipartFileList = createMultipartFiles();
        Long itemId = itemService.saveItem(itemFormDto, multipartFileList);



        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);

        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
        assertEquals(itemFormDto.getItemNm(),item.getItemNm());
        assertEquals(itemFormDto.getItemStatus(),item.getItemStatus());
        assertEquals(itemFormDto.getItemDetail(), item.getItemDetail());
        assertEquals(itemFormDto.getPrice(), item.getPrice());
        assertEquals(itemFormDto.getStockNumber(), item.getStockNumber());

        // 로깅 추가
        for (int i = 0; i < multipartFileList.size(); i++) {
            String originalFilename = multipartFileList.get(i).getOriginalFilename();
            String imgName = itemImgList.get(i).getImgName();
            System.out.println("Original: " + originalFilename + ", ImgName: " + imgName);
        }
    }
}