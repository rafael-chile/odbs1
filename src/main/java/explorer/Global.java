package explorer;

public class Global {
	
	public static final String catPerson = "Person";
	public static final String catProduction = "Production";
	public static final String catCompany = "Company";
	public static final String catCharacter = "Character";
	public static final String catSeries = "Series";
	public static final String catSeason = "Season";
	public static final String catEpisode = "Episode";
	public static final String catTag = "Tag";
	
	public static final int roleIndex = 0;
	public static final int productionIndex = 1;
	public static final int tagIndex = 2;

	public static final String filterRole = "Filter by role:";
	public static final String filterProduction = "Filter by kind of production:";
	public static final String filterTag = "Filter by kind of tag:";
	
	public static final String roleAll = "All roles";
	public static final String roleActors = "Only actors";
	public static final String roleProducers = "Only producers";
	public static final String roleWriters = "Only writers";
	public static final String roleCinematographers = "Only cinematographers";
	public static final String roleComposers = "Only composers";
	public static final String roleCostumeDesigners = "Only costume designers";
	public static final String roleDirectors = "Only directors";
	public static final String roleEditors = "Only editors";
	public static final String roleMiscellaneous = "Only miscellaneous crew members";
	public static final String roleProductionDesigners = "Only production designers";
	public static final String roleGuests = "Only guests";
	
	public static final String prodkindSeries = "Only series";
	public static final String prodkindSeasons = "Only seasons";
	public static final String prodkindEpisodes = "Only episodes";
	
	public static final String tagAll = "All tags";
	public static final String tagGenres = "Only genres";
	
	//TANU
	public static final String queryPersonToProduction = "In which productions did he or she worked?";
	public static final String queryPersonToPerson = "Who did he work with?";
	public static final String queryPersonToCharacter = "What characters did her or she portrayed?";
	//ADRI
	public static final String queryCompanyToCompany = "What companies are based in the same country?";
	public static final String queryCompanyToProduction = "What productions were produced by them?";
	public static final String queryCharacterToPerson = "What actors portrayed him or her?";
	public static final String queryCharacterToProduction = "In which productions did he or she appeared?";
	public static final String queryCharacterToCharacter = "Which characters appeared in a common production?";
	public static final String querySeriesToSeason = "What seasons does it include?";
	public static final String querySeriesToEpisode = "What episodes does it include?";
	public static final String querySeasonToSeries = "Which series does it belong to?";
	public static final String querySeasonToEpisode = "Which episodes does it include?";
	public static final String querySeasonToSeason = "Which are all the seasons in the series?";
	//RAFA
	public static final String queryEpisodeToSeason = "Which season does it belong to?";
	public static final String queryEpisodeToSeries = "Which series does it belong to?";
	public static final String queryEpisodeToEpisode = "Which are all the episodes in the series?";
	public static final String queryProductionToPerson = "Who worked on it?";
	public static final String queryProductionToCharacter = "What characters appeared on it?";
	public static final String queryProductionToProduction_Year = "What productions were produced in the same year?";
	public static final String queryProductionToTag = "What tags are attached to it?";
	public static final String queryProductionToCompany = "What companies produced it?";
	public static final String queryProductionToProduction_Company = "What productions were made by the same company?";
	public static final String queryProductionToProduction_Country = "What productions were made in the same country?";
	public static final String queryTagToProduction = "Which productions are tagged with this?";
	
	public static final int stateCategorySelection = 1;
	public static final int stateAnswerSelection = 2;
	public static final int stateSchemaSelection = 3;
	public static final int stateFilterSelection = 4;
	public static final int stateReadyforQuerying = 5;
	public static final int stateQueryingDatabase = 6;
	public static final int stateWanderingBack = 7;
	
	public static final String newline = System.getProperty("line.separator");
	public static final String logSeparator = "---------------------------------";
	public static Integer limit = 0;
	public static final String owlfile = "src/main/resources/imdb/seriology.owl";
	public static final String obdafile = "src/main/resources/imdb/seriology.obda";
}