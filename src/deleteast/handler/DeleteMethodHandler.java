package deleteast.handler;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;

import deleteast.analysis.DeleteMethodAnalyzer;
import deleteast.model.ModelProvider;
import deleteast.model.ProgramElement;
import deleteast.view.Viewer;

public class DeleteMethodHandler {

	@Inject
	private ESelectionService selectionService;
	@Inject
	private EPartService epartService;

	@Execute
	public void execute() {
		System.out.println("DelProgElemHandler!!");
		MPart findPart = epartService.findPart(Viewer.ID);
		Object findPartObj = findPart.getObject();
		if (findPartObj instanceof Viewer) {
			if (selectionService.getSelection() instanceof ProgramElement) {
				ProgramElement p = (ProgramElement) selectionService.getSelection();
				new DeleteMethodAnalyzer(p);
				ModelProvider.INSTANCE.getProgramElements().remove(p);
				Viewer v = (Viewer) findPartObj;
				v.refresh();
			}
		}
	}

}
