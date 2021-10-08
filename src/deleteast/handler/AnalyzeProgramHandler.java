package deleteast.handler;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import deleteast.analysis.ProjectAnalyzer;
import deleteast.model.ModelProvider;
import deleteast.view.Viewer;

public class AnalyzeProgramHandler {

	@Inject
	private EPartService ePartService;

	@Execute
	public void execute() {
		System.out.println("TableRefreshHandler!!");
		MPart findPart = ePartService.findPart(Viewer.ID);
		Object findPartObj = findPart.getObject();

		if (findPartObj instanceof Viewer) {
			ModelProvider.INSTANCE.clearProgramElements();
			ProjectAnalyzer analyzer = new ProjectAnalyzer();
			analyzer.analyze();
			Viewer viewer = (Viewer) findPartObj;
			viewer.setInput(ModelProvider.INSTANCE.getProgramElements());
			viewer.refresh();
		}
	}

}
