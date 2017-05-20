package com.ryan.service;

import java.util.Map;

import com.ryan.dto.OrderDTO;

public interface OrderService {

	public Map<String, Object> pageOrderList(Integer page, Integer length, String searchVal, String orderDir,
			String orderCol);

	public OrderDTO save(OrderDTO orderDTO);

	public void deleteByPrimaryKey(Long id);
}
