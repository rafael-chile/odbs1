package explorer;
public class ExplorerData {
	private static ExplorerDataInstance instance = null;
	public ExplorerData() {
	}
	public static ExplorerDataInstance GetInstance() {
		if(instance == null) {
			instance = new ExplorerDataInstance();
		}
		return instance;
	}
}