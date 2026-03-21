package tacos.data;

import org.springframework.data.repository.CrudRepository;

import tacos.domainEntity.Taco;

public interface TacoRepository 
         extends CrudRepository<Taco, Long> {

}
