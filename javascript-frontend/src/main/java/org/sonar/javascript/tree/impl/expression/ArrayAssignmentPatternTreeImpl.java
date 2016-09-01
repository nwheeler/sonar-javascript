package org.sonar.javascript.tree.impl.expression;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.sonar.sslr.api.typed.Optional;
import java.util.Iterator;
import org.sonar.javascript.tree.impl.JavaScriptTree;
import org.sonar.javascript.tree.impl.SeparatedList;
import org.sonar.plugins.javascript.api.symbols.TypeSet;
import org.sonar.plugins.javascript.api.tree.Tree;
import org.sonar.plugins.javascript.api.tree.expression.ArrayAssignmentPatternTree;
import org.sonar.plugins.javascript.api.tree.lexical.SyntaxToken;
import org.sonar.plugins.javascript.api.visitors.DoubleDispatchVisitor;

public class ArrayAssignmentPatternTreeImpl extends JavaScriptTree implements ArrayAssignmentPatternTree {

  private final SyntaxToken openBracketToken;
  private final SeparatedList<Optional<Tree>> elements;
  private final SyntaxToken closeBracketToken;

  public ArrayAssignmentPatternTreeImpl(SyntaxToken openBracketToken, SeparatedList<Optional<Tree>> elements, SyntaxToken closeBracketToken) {
    this.openBracketToken = openBracketToken;
    this.elements = elements;
    this.closeBracketToken = closeBracketToken;
  }

  @Override
  public Kind getKind() {
    return Kind.ARRAY_ASSIGNMENT_PATTERN;
  }

  @Override
  public Iterator<Tree> childrenIterator() {
    return Iterators.concat(
      Iterators.singletonIterator(openBracketToken),
      elements.elementsAndSeparators(new ElidedElementFilter()),
      Iterators.singletonIterator(closeBracketToken)
    );
  }

  @Override
  public SyntaxToken openBracketToken() {
    return openBracketToken;
  }

  @Override
  public SeparatedList<Optional<Tree>> elements() {
    return elements;
  }

  @Override
  public SyntaxToken closeBracketToken() {
    return closeBracketToken;
  }

  @Override
  public TypeSet types() {
    return TypeSet.emptyTypeSet();
  }

  @Override
  public void accept(DoubleDispatchVisitor visitor) {
    visitor.visitArrayAssignmentPattern(this);
  }

  private static class ElidedElementFilter implements Function<Optional<Tree>, Tree> {

    @Override
    public Tree apply(Optional<Tree> e) {
      if (e.isPresent()) {
        return e.get();
      }
      return null;
    }

  }
}
