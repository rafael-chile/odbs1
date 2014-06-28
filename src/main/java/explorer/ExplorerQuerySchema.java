package explorer;

public class ExplorerQuerySchema {
	private String fromCategory;
	private String toCategory;
	private Boolean[] filters;
	private String caption;
	private int id;
	
	public ExplorerQuerySchema(String fromCategory, String toCategory, String caption, Boolean roleFilter,
			Boolean productionFilter, Boolean tagFilter, int id) {
		this.fromCategory = fromCategory;
		this.toCategory = toCategory;
		this.caption = caption;
		filters = new Boolean[3];
		filters[Global.roleIndex] = roleFilter;
		filters[Global.productionIndex] = productionFilter;
		filters[Global.tagIndex] = tagFilter;
		this.id = id;
	}

	public String getFromCategory() {
		return fromCategory;
	}
	public String getToCategory() {
		return toCategory;
	}
	public String getCaption() {
		return caption;
	}
	public int getId() {
		return id;
	}
	public Boolean hasRoleFilter() {
		return filters[Global.roleIndex];
	}
	public Boolean hasProductionFilter() {
		return filters[Global.productionIndex];
	}
	public Boolean hasTagFilter() {
		return filters[Global.tagIndex];
	}
}
