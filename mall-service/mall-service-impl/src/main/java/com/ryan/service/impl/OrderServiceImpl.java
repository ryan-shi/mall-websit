package com.ryan.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ryan.domain.Address;
import com.ryan.domain.Order;
import com.ryan.domain.OrderItem;
import com.ryan.domain.User;
import com.ryan.dto.AddressDTO;
import com.ryan.dto.OrderDTO;
import com.ryan.dto.OrderItemDTO;
import com.ryan.dto.UserDTO;
import com.ryan.repository.OrderItemRepository;
import com.ryan.repository.OrderRepository;
import com.ryan.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
	
	private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Autowired
	OrderRepository orderRepository;
	@Autowired
	OrderItemRepository orderItemRepository;
	
	@Override
	public Map<String, Object> pageOrderList(Integer page, Integer length, String searchVal, String orderDir,
			String orderCol) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrderDTO save(OrderDTO orderDTO) {
		Order order=orderDTOToOrder(orderDTO);
		log.info("address:{}",order.getAddress().getId());
		log.info("OrderItems:{}",order.getOrderItems().size());
		List<OrderItem> orderItems=order.getOrderItems();
		if(!CollectionUtils.isEmpty(orderItems)){
			for (OrderItem orderItem : orderItems) {
				orderItemRepository.save(orderItem);
			}
		}
		Order savedOrder=orderRepository.save(order);
		return orderToOrderDTO(savedOrder);
	}

	@Override
	public void deleteByPrimaryKey(Long id) {
		// TODO Auto-generated method stub

	}

	public Order orderDTOToOrder(OrderDTO orderDTO) {
		Order order = new Order();
		BeanUtils.copyProperties(orderDTO, order);
		Address address = new Address();
		AddressDTO addressDTO = orderDTO.getAddressDTO();
		log.info("addressDTO:{}",addressDTO.getId());
		if (addressDTO != null)
			BeanUtils.copyProperties(addressDTO, address);
		order.setAddress(address);

		User user = new User();
		UserDTO userDTO = orderDTO.getUserDTO();
		if (userDTO != null)
			BeanUtils.copyProperties(userDTO, user);
		order.setUser(user);

		List<OrderItemDTO> orderItemDTOs = orderDTO.getOrderItemDTOs();
		log.info("orderItemDTOs {}",orderItemDTOs.size());
		List<OrderItem> orderItems = new ArrayList<>();
		if (!CollectionUtils.isEmpty(orderItemDTOs)) {
			for (OrderItemDTO orderItemDTO : orderItemDTOs) {
				OrderItem orderItem = new OrderItem();
				BeanUtils.copyProperties(orderItemDTO, orderItem);
				orderItems.add(orderItem);
			}
		}
		order.setOrderItems(orderItems);
		return order;
	}
	
	public OrderDTO orderToOrderDTO(Order order){
		OrderDTO orderDTO=new OrderDTO();
		BeanUtils.copyProperties(order, orderDTO);
		return orderDTO;
	}
}
