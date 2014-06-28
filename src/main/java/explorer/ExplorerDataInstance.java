package explorer;
import java.util.*;

public class ExplorerDataInstance {
	private ArrayList<ExplorerQuerySchema> schema;
	
	public ExplorerDataInstance() {
		schema = new ArrayList<ExplorerQuerySchema>();
		schema.add(new ExplorerQuerySchema(Global.catTag, Global.catProduction, Global.queryTagToProduction,
				false, true, false, 0));
		schema.add(new ExplorerQuerySchema(Global.catPerson, Global.catProduction, Global.queryPersonToProduction,
				true, true, false, 1));
		schema.add(new ExplorerQuerySchema(Global.catPerson, Global.catPerson, Global.queryPersonToPerson,
				true, true, false, 2));
		schema.add(new ExplorerQuerySchema(Global.catPerson, Global.catCharacter, Global.queryPersonToCharacter,
				false, false, false, 3));
		schema.add(new ExplorerQuerySchema(Global.catCompany, Global.catCompany, Global.queryCompanyToCompany,
				false, false, false, 4));
		schema.add(new ExplorerQuerySchema(Global.catCompany, Global.catProduction, Global.queryCompanyToProduction,
				false, true, false, 5));
		schema.add(new ExplorerQuerySchema(Global.catCharacter, Global.catPerson, Global.queryCharacterToPerson,
				false, false, false, 6));
		schema.add(new ExplorerQuerySchema(Global.catCharacter, Global.catProduction, Global.queryCharacterToProduction,
				false, true, false, 7));
		schema.add(new ExplorerQuerySchema(Global.catCharacter, Global.catCharacter, Global.queryCharacterToCharacter,
				false, true, false, 8));
		schema.add(new ExplorerQuerySchema(Global.catSeries, Global.catSeason, Global.querySeriesToSeason,
				false, false, false, 9));
		schema.add(new ExplorerQuerySchema(Global.catSeries, Global.catEpisode, Global.querySeriesToEpisode,
				false, false, false, 10));
		schema.add(new ExplorerQuerySchema(Global.catSeries, Global.catPerson, Global.queryProductionToPerson,
				true, false, false, 11));
		schema.add(new ExplorerQuerySchema(Global.catSeries, Global.catCharacter, Global.queryProductionToCharacter,
				false, false, false, 12));
		schema.add(new ExplorerQuerySchema(Global.catSeries, Global.catProduction, Global.queryProductionToProduction_Year,
				false, true, false, 13));
		schema.add(new ExplorerQuerySchema(Global.catSeries, Global.catTag, Global.queryProductionToTag,
				false, false, true, 14));
		schema.add(new ExplorerQuerySchema(Global.catSeries, Global.catCompany, Global.queryProductionToCompany,
				false, false, false, 15));
		schema.add(new ExplorerQuerySchema(Global.catSeries, Global.catProduction, Global.queryProductionToProduction_Company,
				false, true, false, 16));
		schema.add(new ExplorerQuerySchema(Global.catSeries, Global.catProduction, Global.queryProductionToProduction_Country,
				false, true, false, 17));
		schema.add(new ExplorerQuerySchema(Global.catSeason, Global.catSeries, Global.querySeasonToSeries,
				false, false, false, 18));
		schema.add(new ExplorerQuerySchema(Global.catSeason, Global.catEpisode, Global.querySeasonToEpisode,
				false, false, false, 19));
		schema.add(new ExplorerQuerySchema(Global.catSeason, Global.catSeason, Global.querySeasonToSeason,
				false, false, false, 20));
		schema.add(new ExplorerQuerySchema(Global.catSeason, Global.catPerson, Global.queryProductionToPerson,
				true, false, false, 21));
		schema.add(new ExplorerQuerySchema(Global.catSeason, Global.catCharacter, Global.queryProductionToCharacter,
				false, false, false, 22));
		schema.add(new ExplorerQuerySchema(Global.catSeason, Global.catProduction, Global.queryProductionToProduction_Year,
				false, true, false, 23));
		schema.add(new ExplorerQuerySchema(Global.catSeason, Global.catTag, Global.queryProductionToTag,
				false, false, true, 24));
		schema.add(new ExplorerQuerySchema(Global.catSeason, Global.catCompany, Global.queryProductionToCompany,
				false, false, false, 25));
		schema.add(new ExplorerQuerySchema(Global.catSeason, Global.catProduction, Global.queryProductionToProduction_Company,
				false, true, false, 26));
		schema.add(new ExplorerQuerySchema(Global.catSeason, Global.catProduction, Global.queryProductionToProduction_Country,
				false, true, false, 27));
		schema.add(new ExplorerQuerySchema(Global.catEpisode, Global.catSeries, Global.queryEpisodeToSeries,
				false, false, false, 28));
		schema.add(new ExplorerQuerySchema(Global.catEpisode, Global.catSeason, Global.queryEpisodeToSeason,
				false, false, false, 29));
		schema.add(new ExplorerQuerySchema(Global.catEpisode, Global.catEpisode, Global.queryEpisodeToEpisode,
				false, false, false, 30));
		schema.add(new ExplorerQuerySchema(Global.catEpisode, Global.catPerson, Global.queryProductionToPerson,
				true, false, false, 31));
		schema.add(new ExplorerQuerySchema(Global.catEpisode, Global.catCharacter, Global.queryProductionToCharacter,
				false, false, false, 32));
		schema.add(new ExplorerQuerySchema(Global.catEpisode, Global.catProduction, Global.queryProductionToProduction_Year,
				false, true, false, 33));
		schema.add(new ExplorerQuerySchema(Global.catEpisode, Global.catTag, Global.queryProductionToTag,
				false, false, true, 34));
		schema.add(new ExplorerQuerySchema(Global.catEpisode, Global.catCompany, Global.queryProductionToCompany,
				false, false, false, 35));
		schema.add(new ExplorerQuerySchema(Global.catEpisode, Global.catProduction, Global.queryProductionToProduction_Company,
				false, true, false, 36));
		schema.add(new ExplorerQuerySchema(Global.catEpisode, Global.catProduction, Global.queryProductionToProduction_Country,
				false, true, false, 37));
	}
	
	public ArrayList<ExplorerQuerySchema> getSchemas(String category) {
		ArrayList<ExplorerQuerySchema> ans = new ArrayList<ExplorerQuerySchema>();
		for(ExplorerQuerySchema s : schema) {
			if(s.getFromCategory().equals(category)) {
				ans.add(s);
			}
		}
		return ans;
	}
	
	public ArrayList<String> getCategoryList() {
		ArrayList<String> ans = new ArrayList<String>();
		ans.add(Global.catPerson);
		ans.add(Global.catCharacter);
		ans.add(Global.catCompany);
		ans.add(Global.catSeries);
		ans.add(Global.catSeason);
		ans.add(Global.catEpisode);
		ans.add(Global.catTag);
		return ans;
	}
	
	public ArrayList<String> getRoleList() {
		ArrayList<String> ans = new ArrayList<String>();
		ans.add(Global.roleAll);
		ans.add(Global.roleActors);
		ans.add(Global.roleProducers);
		ans.add(Global.roleWriters);
		ans.add(Global.roleCinematographers);
		ans.add(Global.roleComposers);
		ans.add(Global.roleCostumeDesigners);
		ans.add(Global.roleDirectors);
		ans.add(Global.roleEditors);
		ans.add(Global.roleMiscellaneous);
		ans.add(Global.roleProductionDesigners);
		ans.add(Global.roleGuests);
		return ans;
	}
	
	public ArrayList<String> getProductionKindList() {
		ArrayList<String> ans = new ArrayList<String>();
		ans.add(Global.prodkindSeries);
		ans.add(Global.prodkindSeasons);
		ans.add(Global.prodkindEpisodes);
		return ans;
	}
	
	public ArrayList<String> getTagList() {
		ArrayList<String> ans = new ArrayList<String>();
		ans.add(Global.tagAll);
		ans.add(Global.tagGenres);
		return ans;
	}
	
	public String nextCategory(ExplorerQuerySchema schema, String prodselection) {
		String category = schema.getToCategory();
		if(category.equals(Global.catProduction)) {
			if(prodselection.equals(Global.prodkindSeries)) {
				return Global.catSeries;
			} else if(prodselection.equals(Global.prodkindSeasons)) {
				return Global.catSeason;
			} else if(prodselection.equals(Global.prodkindEpisodes)) {
				return Global.catEpisode;
			} else {
				return category;
			}
		} else {
			return category;
		}
	}
	
	public void printinfo() {
		for(ExplorerQuerySchema s : schema) {
			String ans = s.getCaption() + " > ";
			if(s.hasRoleFilter()) {
				ans = ans + "Role ";
			}
			if(s.hasProductionFilter()) {
				ans = ans + "Production ";
			}
			if(s.hasTagFilter()) {
				ans = ans + "Tag ";
			}
			System.out.println(ans);
		}
	}
}
