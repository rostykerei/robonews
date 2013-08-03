package nl.rostykerei.news.manager.dto;

import nl.rostykerei.news.domain.Category;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CategoryDto {

    public CategoryDto() {
        super();
    }

    public CategoryDto(Category category) {
        setId(category.getId());
        setName(category.getName());
        setPriority(category.isPriority());
    }


    private int id;

    @Size(min = 1, max = 255, message = "Name cannot be empty")
    private String name;

    private boolean priority;

    @NotNull
    @Min(value = 1, message = "Parent category cannot be empty")
    private int parentCategoryId;

    public Category toCategory(){
        Category category = new Category();
        category.setId(getId());
        category.setName(getName());
        category.setPriority(isPriority());

        return category;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPriority() {
        return priority;
    }

    public void setPriority(boolean priority) {
        this.priority = priority;
    }

    public int getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(int parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }
}
