package deleteast.view;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import deleteast.model.filter.ProgramElementFilter;
import deleteast.model.labelproviders.ClassNameLabelProvider;
import deleteast.model.labelproviders.MethodNameLabelProvider;
import deleteast.model.labelproviders.ParameterLabelProvider;
import deleteast.model.labelproviders.ProgramElementLabelProvider;
import deleteast.model.labelproviders.ReturnTypeLabelProvider;
import deleteast.model.sorter.ProgramElementSorter;

public class Viewer {

	public final static String ID = "project-ex-1007-type-delete-ast-mezzell.partdescriptor.simpletableview-deletemethodast-1007mezzell";
	public final static String POPUPMENU = "project-ex-1007-type-delete-ast-mezzell.popupmenu.astpopupmenu";
	private static final int BOUNDS = 100;

	private TableViewer viewer;
	private Text searchText;
	private ProgramElementFilter programElementFilter;
	private ProgramElementSorter programElementSorter;

	@Inject
	private ESelectionService selectionService;

	public TableViewer getViewer() {
		return viewer;
	}

	public void setInput(Object data) {
		viewer.setInput(data);
	}
	
	public void refresh() {
		viewer.refresh();
	}

	@PostConstruct
	public void createControls(Composite parent, EMenuService menuService) {
		GridLayout layout = new GridLayout(2, false);
		parent.setLayout(layout);

		createSearchText(parent);
		createViewer(parent);

		menuService.registerContextMenu(viewer.getControl(), POPUPMENU);
		addKeyEventToSearchText();
	}

	private void createSearchText(Composite parent) {
		Label searchLabel = new Label(parent, SWT.NONE);
		searchLabel.setText("Search: ");
		searchText = new Text(parent, SWT.BORDER | SWT.SEARCH);
		searchText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
	}

	private void addKeyEventToSearchText() {
		searchText.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent ke) {
				programElementFilter.setSearchText(searchText.getText());
				viewer.refresh();
			}
		});
	}

	private void createViewer(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		createColumns();
		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
				Object firstElement = selection.getFirstElement();
				selectionService.setSelection(selection.size() == 1 ? firstElement : selection.toArray());
			}
		});

		// define layout for the viewer
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		viewer.getControl().setLayoutData(gridData);

		programElementFilter = new ProgramElementFilter();
		viewer.addFilter(programElementFilter);
		programElementSorter = new ProgramElementSorter();
		viewer.setComparator(programElementSorter);
	}

	private void createColumns() {
		TableViewerColumn column = createTableViewerColumn("Package Name", BOUNDS, 0);
		column.setLabelProvider(new ProgramElementLabelProvider(searchText));

		column = createTableViewerColumn("Class Name", BOUNDS, 1);
		column.setLabelProvider(new ClassNameLabelProvider(searchText));

		column = createTableViewerColumn("Method Name", BOUNDS, 2);
		column.setLabelProvider(new MethodNameLabelProvider(searchText));

		column = createTableViewerColumn("Returns Void", BOUNDS, 3);
		column.setLabelProvider(new ReturnTypeLabelProvider());

		column = createTableViewerColumn("ParameterSize", BOUNDS, 4);
		column.setLabelProvider(new ParameterLabelProvider());
	}

	private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		column.addSelectionListener(getSelectionAdapter(column, colNumber));
		return viewerColumn;
	}

	private SelectionAdapter getSelectionAdapter(final TableColumn column, final int index) {
		SelectionAdapter selectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				programElementSorter.setColumn(index);
				int dir = programElementSorter.getDirection();
				viewer.getTable().setSortDirection(dir);
				viewer.getTable().setSortColumn(column);
				viewer.refresh();
			}
		};
		return selectionAdapter;
	}

	@PreDestroy
	public void dispose() {
	}

	@Focus
	public void setFocus() {
	}

}
