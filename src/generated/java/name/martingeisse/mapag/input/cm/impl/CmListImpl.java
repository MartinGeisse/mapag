package name.martingeisse.mapag.input.cm.impl;

import com.google.common.collect.ImmutableList;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;
import java.util.ArrayList;

import name.martingeisse.mapag.input.cm.CmNode;
import name.martingeisse.mapag.input.cm.CmList;

public final class CmListImpl<T extends CmNode> extends ASTWrapperPsiElement implements PsiCm, CmList<T> {

    private final TokenSet elementTypes;
    private final Class<T> elementClass;

    public CmListImpl(@NotNull ASTNode node, TokenSet elementTypes, Class<T> elementClass) {
        super(node);
        this.elementTypes = elementTypes;
        this.elementClass = elementClass;
    }

    @Override
    public <S extends CmNode> CmList<S> cast(Class<S> subclass) {
        if (!elementClass.isAssignableFrom(subclass)) {
            throw new ClassCastException(subclass.getName() + " is not a subclass of " + elementClass.getName());
        }
        return (CmList)this;
    }

    @Override
    public final List<T> getAll() {
        List<T> list = new ArrayList<>();
        addAllTo(list);
        return list;
	}

    @Override
	public final void addAllTo(List<T> list) {
        foreach(list::add);
	}

    @Override
	public final void addAllTo(ImmutableList.Builder<T> builder) {
        foreach(builder::add);
	}

    @Override
    public final void foreach(Consumer<T> consumer) {
        InternalPsiUtil.foreachChild(this, child -> {
            if (elementTypes.contains(child.getNode().getElementType())) {
                consumer.accept(elementClass.cast(child));
                return;
            }
            if (child instanceof CmListImpl<?> && child.getNode().getElementType() == getNode().getElementType()) {
                CmListImpl<?> typedChild = (CmListImpl<?>)child;
                typedChild.cast(elementClass).foreach(consumer);
            }
        });
    }

}
