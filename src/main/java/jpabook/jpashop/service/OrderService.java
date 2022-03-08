package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderRepositorySpring;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderRepositorySpring orderRepositorySpring;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {

        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        orderRepository.save(order);

        return order.getId();
    }
    
    @Transactional
    public void save(Long memberId, Long itemId, int count) {
    	
    	   //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);
        

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);
        
        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        orderRepository.save(order);
    }
    
    
    @Transactional
    public void remove(Long orderId) {
    	//주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        
        orderRepository.remove(order);
    }

    /**
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        //주문 취소
        order.cancel();
    }

    //검색
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByString(orderSearch);
    }
    
    public void findOrder(Long orderId) {
    	
    	Order order = orderRepository.findOne(orderId);
    	
    	List<OrderItem> orderItems = order.getOrderItems();
    
//    	System.out.println("get() 호출 전 초기화 되지 않음 확인");
    	
    	orderItems.get(0);
    	
    }
    
    public void findAll() {
    	
    	List<Order> order =orderRepositorySpring.findAll();
    	
    	for(Order ord : order) {
    		System.out.println( "주문번호 : " + ord.getId() + ", 주문아이템 갯수 : " +   ord.getOrderItems().size() );
    		System.out.println( "주문번호 : " + ord.getId() + ", 주문아이템 갯수 : " +   ord.getMember() );
    	}
    }
    
public void findAllFetch() {
    	
    	
    	List<Order> order =orderRepository.findAllWithItem();
    	
    	for(Order ord : order) {
    		System.out.println( "주문번호 : " + ord.getId() + ", 주문아이템 갯수 : " +   ord.getOrderItems().size() );
    	}
    }
}
