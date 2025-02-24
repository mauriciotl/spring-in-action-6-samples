package tacos.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import lombok.extern.slf4j.Slf4j;
import tacos.model.Ingredient;
import tacos.model.Ingredient.Type;
import tacos.model.Taco;
import tacos.model.TacoOrder;

import javax.validation.Valid;

import org.springframework.validation.Errors;

@Slf4j
@Controller
@RequestMapping("/design")
/*
The @SessionAttributes("tacoOrder") annotation indicates that the tacoOrder attribute
will be stored in the session. This allows the TacoOrder object to persist across
multiple requests during the taco design process.
 */
@SessionAttributes("tacoOrder")
public class DesignTacoController {

    /*
    Annotated with @ModelAttribute, this method is called before every request handler method in the controller.
    It populates the Model object with a list of ingredients, categorized by their type (e.g., WRAP, PROTEIN, VEGGIES, etc.).
    The ingredients are added to the model as attributes, making them available to the view (e.g., a Thymeleaf template).
      In other words: Adds filtered lists of ingredients to the Model (e.g., wrap, protein, etc.).
                      The full ingredients list is not added to the model unless explicitly done so
     */
    @ModelAttribute
    public void addIngredientsToModel(Model model) {
        List<Ingredient> ingredients = Arrays.asList(
                new Ingredient("FLTO", "Flour Tortilla", Type.WRAP),
                new Ingredient("COTO", "Corn Tortilla", Type.WRAP),
                new Ingredient("GRBF", "Ground Beef", Type.PROTEIN),
                new Ingredient("CARN", "Carnitas", Type.PROTEIN),
                new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES),
                new Ingredient("LETC", "Lettuce", Type.VEGGIES),
                new Ingredient("CHED", "Cheddar", Type.CHEESE),
                new Ingredient("JACK", "Monterrey Jack", Type.CHEESE),
                new Ingredient("SLSA", "Salsa", Type.SAUCE),
                new Ingredient("SRCR", "Sour Cream", Type.SAUCE)
        );

        Type[] types = Ingredient.Type.values();

        /*
        El siguiente for regresará varias listas, en este caso 5 listas (wrap, protein,
        veggies, cheese, sauce), cada lista contendrá los ingredientes correspondientes
        a cada tipo de ingredientes. NO ES UN SORT JEJEJE.
         */
        for (Type type : types) {
            System.out.println("======= type: " + type.toString().toLowerCase());
            model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
        }
    }

    /*
    Annotated with @ModelAttribute(name = "tacoOrder"), this method creates and returns a new TacoOrder object.
    The TacoOrder object is added to the model and session (@SessionAttributes("tacoOrder")), allowing it to be shared across multiple requests.
    */
    @ModelAttribute(name = "tacoOrder")
    public TacoOrder order() {
        return new TacoOrder();
    }

    /*
    Annotated with @ModelAttribute(name = "taco"), this method creates and returns a new Taco object.
    The Taco object is added to the model, representing the taco being designed by the user.
     */
    @ModelAttribute(name = "taco")
    public Taco taco() {
        return new Taco();
    }

    /*
    showDesignForm():
  Handles GET requests to /design (annotated with @GetMapping).
  Returns the view name "design", which corresponds to a Thymeleaf template (e.g., design.html) where the user can design their taco.
     */
    @GetMapping
    public String showDesignForm() {
        return "design";
    }


    /* Handles POST requests to /design (annotated with @PostMapping).
Validates the Taco object using the @Valid annotation and checks for validation errors using the Errors object.
If there are validation errors, the user is redirected back to the design form ("design" view).
If there are no errors, the Taco object is added to the TacoOrder object, and the user is redirected to the
/orders/current URL to proceed with the order.
     */
    @PostMapping
    public String processTaco( @Valid Taco taco, Errors errors,
            @ModelAttribute TacoOrder tacoOrder) {

        if (errors.hasErrors()) {
            return "design";
        }

        tacoOrder.addTaco(taco);
        log.info("Processing taco: {}", taco);

        return "redirect:/orders/current";
    }


    /*
    A helper method that filters a list of ingredients by their type (e.g., WRAP, PROTEIN, etc.).
    Used in the addIngredientsToModel method to categorize ingredients for the view.
    MAU NOTE. Es decir que regresará una lista llena de ingredientes
     del tipo espefico de type que se le especifíque
    en el parametro Type type.
    */
    private Iterable<Ingredient> filterByType(List<Ingredient> ingredients, Type type)
    {
        return ingredients
                .stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());
    }

}
