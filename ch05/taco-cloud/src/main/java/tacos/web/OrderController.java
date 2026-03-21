package tacos.web;
import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import tacos.domainEntity.*;
import tacos.data.OrderRepository;

@Controller
@RequestMapping("/orders")
@SessionAttributes("order")
@Slf4j
public class OrderController {

  private OrderRepository orderRepo;

  public OrderController(OrderRepository orderRepo) {
    this.orderRepo = orderRepo;
  }

//  @ModelAttribute(name = "order")
//  public TacoOrder order() {
//    return new TacoOrder();
//  }


  @GetMapping("/current")
  public String orderForm(@AuthenticationPrincipal User user,
      @ModelAttribute("order") TacoOrder order) {

    System.out.println("=== User fullname  : " + user.getFullname());
    System.out.println("=== User street    : " + user.getStreet());


    if (order.getDeliveryName() == null  || order.getDeliveryName().isBlank()) {
      order.setDeliveryName(user.getFullname());
      System.out.println("=== Setting deliveryName to: " + user.getFullname());
    }
    if (order.getDeliveryStreet() == null || order.getDeliveryStreet().isBlank()) {
      order.setDeliveryStreet(user.getStreet());
      System.out.println("=== Setting street to: " + user.getStreet());
    }
    if (order.getDeliveryCity() == null || order.getDeliveryCity().isBlank()) {
      order.setDeliveryCity(user.getCity());
    }
    if (order.getDeliveryState() == null || order.getDeliveryState().isBlank()) {
      order.setDeliveryState(user.getState());
    }
    if (order.getDeliveryZip() == null || order.getDeliveryZip().isBlank()) {
      order.setDeliveryZip(user.getZip());
    }

    return "orderForm";
  }

  @PostMapping
  public String processOrder(@Valid TacoOrder order, Errors errors,
      SessionStatus sessionStatus,
      @AuthenticationPrincipal User user) {

    if (errors.hasErrors()) {
      return "orderForm";
    }

    order.setUser(user);

    orderRepo.save(order);

    log.info("   --- The order has been saved");
    sessionStatus.setComplete();

    return "redirect:/";
  }

}
