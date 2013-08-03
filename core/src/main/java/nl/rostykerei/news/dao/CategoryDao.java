package nl.rostykerei.news.dao;

import nl.rostykerei.news.domain.Category;

import java.util.List;

public interface CategoryDao {

    Category getById(int id);

    Category createRoot(String name);

    int create(Category category, Category parentCategory);

    int create(Category category, int parentCategoryId);

    List<Category> getAll();

    Category getParent(Category category);

    List<Category> getChildren(Category category);

    List<Category> getChildren(Category category, int depth);

    boolean hasChildren(Category category);

    boolean hasParent(Category category);

    void delete(Category category);
}
