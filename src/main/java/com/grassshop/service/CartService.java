package com.grassshop.service;

import com.grassshop.dto.CartItemDto;
import com.grassshop.entity.Account;
import com.grassshop.entity.Cart;
import com.grassshop.entity.CartItem;
import com.grassshop.entity.Item;
import com.grassshop.repository.AccountRepository;
import com.grassshop.repository.CartItemRepository;
import com.grassshop.repository.CartRepository;
import com.grassshop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {
    private final ItemRepository itemRepository;
    private final AccountRepository accountRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    /* 장바구나 추가 */
    public Long addCart(CartItemDto cartItemDto, String email) {
        Item item = itemRepository.findById(cartItemDto.getItemId()).orElseThrow(EntityNotFoundException::new);
        Account account = accountRepository.findByEmail(email);

        Cart cart = cartRepository.findByAccountId(account.getId());
        if (cart == null) {
            cart = Cart.createCart(account);
            cartRepository.save(cart);
        }
        CartItem SavedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());
        if (SavedCartItem != null) {
            SavedCartItem.addCount(cartItemDto.getCount());
            return SavedCartItem.getId();
        }else {
            CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
            cartItemRepository.save(cartItem);
            return cartItem.getId();
        }
    }
}
