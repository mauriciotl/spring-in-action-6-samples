package tacos.data;

import org.springframework.data.repository.CrudRepository;

import tacos.domainEntity.TacoOrder;

public interface OrderRepository 
         extends CrudRepository<TacoOrder, Long> {

}
