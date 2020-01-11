package sample.model;

public interface Bindable {
    void setBinding(Bindable other);
    Bindable getBound();
}
