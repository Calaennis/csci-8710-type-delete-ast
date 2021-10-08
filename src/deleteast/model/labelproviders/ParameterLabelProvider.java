/**
 * @(#) ParameterLabelProvider.java
 */
package deleteast.model.labelproviders;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import deleteast.model.ProgramElement;

/**
 * @since J2SE-1.8
 */
public class ParameterLabelProvider extends ColumnLabelProvider {
   @Override
   public String getText(Object element) {
      ProgramElement p = (ProgramElement) element;
      return String.valueOf(p.getParameterSize());
   }
}
