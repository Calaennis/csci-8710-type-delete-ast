/**
 * @(#) MethodNameLabelProvider.java
 */
package deleteast.model.labelproviders;

import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.widgets.Text;

import deleteast.model.ProgramElement;

/**
 * @since J2SE-1.8
 */
public class MethodNameLabelProvider extends ProgramElementLabelProvider {
   public MethodNameLabelProvider(final Text searchText) {
      this.searchText = searchText;
   }

   @Override
   public void update(ViewerCell cell) {
      super.update(cell);
   }

   protected String getCellText(ViewerCell cell) {
      ProgramElement p = (ProgramElement) cell.getElement();
      String cellText = p.getMethodName();
      return cellText;
   }
}
