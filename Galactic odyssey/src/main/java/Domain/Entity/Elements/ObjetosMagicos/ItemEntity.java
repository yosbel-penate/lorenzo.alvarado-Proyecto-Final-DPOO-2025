package Domain.Entity.Elements.ObjetosMagicos;

import com.almasb.fxgl.entity.component.Component;

public class ItemEntity extends Component {

    private Item item;

    public ItemEntity() {
        // constructor vacío requerido por FXGL (si lo usas sin parámetros)
    }

    public ItemEntity(Item item) {
        this.item = item;
    }

    @Override
    public void onAdded() {
        if (item == null) {
            throw new IllegalStateException("Item no inicializado en ItemEntity.");
        }
        getEntity().getProperties().setValue("item", item);
    }

    public Item getItem() {
        return item;
    }
    public void setItem(Item item) {
        this.item = item;
    }
}
