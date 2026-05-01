package tacos.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tacos.Ingredient;
import tacos.data.IngredientRepository;

@RestController
@RequestMapping(path="/api/ingredients", produces="application/json")
@CrossOrigin(origins="http://localhost:8080")
public class IngredientController {

  private IngredientRepository repo;

  @Autowired
  public IngredientController(IngredientRepository repo) {
    this.repo = repo;
  }

  @GetMapping
  public Iterable<Ingredient> allIngredients() {
    return repo.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Ingredient> ingredientById(@PathVariable String id) {
    return repo.findById(id)
            .map(ingredient -> new ResponseEntity<>(ingredient, HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PostMapping(consumes="application/json")
  @ResponseStatus(HttpStatus.CREATED)
  public Ingredient postIngredient(@RequestBody Ingredient ingredient) {
    return repo.save(ingredient);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Ingredient> putIngredient(@PathVariable String id, @RequestBody Ingredient ingredient) {
    // 1. Validate that the resource exists
    if (!repo.existsById(id)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // 2. Ensure ID consistency
    if (!ingredient.getId().equals(id)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>(repo.save(ingredient), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteIngredient(@PathVariable String id) {
    // 1. Check existence before deleting
    if (!repo.existsById(id)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    repo.deleteById(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

}