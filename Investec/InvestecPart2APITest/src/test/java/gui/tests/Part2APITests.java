package gui.tests;

import com.jayway.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.testng.Assert;
import org.testng.annotations.Test;
import static com.jayway.restassured.RestAssured.get;


public class Part2APITests {

    @Test
    public void getPeople() throws JSONException {

        //make get request to people
        Response resp = get("https://swapi.dev/api/people/");

        //Fetching response in JSON
        JSONArray jsonResponse = new JSONArray(resp.asString());

        String skincolor = jsonResponse.getJSONObject(0).getString("skin_color");

        //Asserting that skin color
        Assert.assertEquals(skincolor, "white, blue");
    }

}
