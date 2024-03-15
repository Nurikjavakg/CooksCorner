package neo.cookscorner.dto.ingredient;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class IngredientResponse {
    List<String> ingredientNames;
    List<String> quantities;

    public IngredientResponse(List<String> ingredientNames, List<String> quantities) {
        this.ingredientNames = ingredientNames;
        this.quantities = quantities;
    }
}
