package controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dao.DaoFactory;
import view.OrderLineView;
import model_class.OrderLine;
import model_class.Product;

@Component
public class OrderLineController {

	@Autowired
	ProductController productController;
	
	@Autowired
	OrderLineView orderLineView;

	@Autowired
	OrderLine orderLine;
	
	// OrderLine Controller
	public List<OrderLine> createOrderLines(int orderId) {
		List<OrderLine> orderLineList = new ArrayList<>();
		List<Product> productList = new ArrayList<>();

		boolean addProduct = true;
		while (addProduct) {
			Product product = productController.selectProductFromList();
			if (productList.contains(product)) {
				orderLineView.productAlreadyAdded();
			} else {
				orderLine.setProduct(product);
				orderLine.setAmount(orderLineView.requestAmount(product
						.getStock()));
				orderLine.setOrderId(orderId);
				DaoFactory.getOrderLineDao().createOrderLine(orderLine);
				System.out.println("OrderLineList is before: " + orderLineList);
				orderLineList.add(orderLine);
				System.out.println("OrderLineList is after: " + orderLineList);
				productList.add(product);
			}
			if (orderLineView.addMoreProducts() == false)
				addProduct = false;
		}
		System.out.println("Returning the following orderLine: " + orderLineList);
		return orderLineList;
	}

	public String getProductNameOfOrderLine(OrderLine orderLine) {
		int productId = orderLine.getProduct().getId();
		String productName = DaoFactory.getProductDao().readProductById(productId)
				.getName();
		return productName;
	}

}
