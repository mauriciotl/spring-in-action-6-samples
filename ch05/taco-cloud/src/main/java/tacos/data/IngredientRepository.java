package tacos.data;

import org.springframework.data.repository.CrudRepository;

import tacos.domainEntity.Ingredient;

public interface IngredientRepository 
         extends CrudRepository<Ingredient, String> {

}
