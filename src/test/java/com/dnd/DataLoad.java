package com.dnd;

import com.dnd.dao.MonsterDao;
import com.dnd.model.Monster;
import com.dnd.model.enums.Ability;
import com.dnd.model.enums.DamageType;
import com.dnd.model.enums.Skill;
import com.google.gson.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataLoad {

    @Autowired
    private MonsterDao dao;

    @Test
    public void getMonsters() throws URISyntaxException, IOException {
        StringBuilder sb = new StringBuilder();
        PrintWriter pw = new PrintWriter("monsters.txt");
        String url = "http://www.dnd5eapi.co/api/monsters/";
        for (int i = 1; i <= 325; i++) {
            sb.append(IOUtils.toString(new URI(url + String.valueOf(i))));
        }
        pw.println(sb.toString());
        pw.close();
    }

    @Test
    public void jsonToMonsters() throws IOException, SQLException {
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        JsonParser parser = new JsonParser();
        String json = FileUtils.readFileToString(new File(this.getClass().getResource("/monsters.txt").getFile()));
        JsonArray array = parser.parse(json).getAsJsonArray();

        int count = 0;
        for (JsonElement element : array) {
            JsonObject jsonObject = element.getAsJsonObject();
            System.out.println(++count);
            Monster monster = gson.fromJson(jsonObject.toString(), Monster.class);
            for (Ability a : Ability.values()) {
                JsonElement ability = jsonObject.get(a.name().toLowerCase());
                JsonElement saving = jsonObject.get(a.name().toLowerCase() + "_save");
                if (ability != null) {
                    monster.addAbility(a.name(), ability.getAsInt());
                }
                if (saving != null) {
                    monster.addSavingThrow(a.name(), saving.getAsInt());
                }
            }
            for (Skill s : Skill.values()) {
                JsonElement skill = jsonObject.get(s.name().toLowerCase());
                if (skill != null) {
                    monster.addSkill(s.name(), skill.getAsInt());
                }
            }
            dao.createMonster(monster, 100);
        }
    }

    @Test
    public void getMonstersFromDatabase() {
        Monster monster = dao.getMonsterByName("Adult Brass Dragon");
        System.out.println(monster);
    }
}
