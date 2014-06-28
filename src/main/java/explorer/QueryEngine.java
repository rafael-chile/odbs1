package explorer;
public class QueryEngine {
	private static QueryEngineInstance instance = null;
	public QueryEngine() {
	}
	public static QueryEngineInstance GetInstance() {
		if(instance == null) {
			try {
				instance = new QueryEngineInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return instance;
	}
}