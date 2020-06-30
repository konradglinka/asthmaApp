package OWM;

import Objects.WeatherMeasureOWM;
import Repositories.FromDB.OWMClaudinesTranslatorRepository;
import net.aksingh.owmjapis.api.APIException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

public class WeatherMeasuresFactory {

    private ArrayList<WeatherMeasureOWM> weatherMeasuresListOWM =new ArrayList<>();
    private DataFromOWM dataFromOWM;
    private int howManyMeasures;

    public WeatherMeasuresFactory(int id, int howManyMeasures, OWMClaudinesTranslatorRepository owmClaudinesTranslatorRepository) throws IOException, APIException, ParseException {
        this.howManyMeasures=howManyMeasures;
        dataFromOWM=new DataFromOWM(id);
        fillMeasuresist(owmClaudinesTranslatorRepository);
    }

    public void fillMeasuresist(OWMClaudinesTranslatorRepository owmClaudinesTranslatorRepository) throws FileNotFoundException, APIException, ParseException {
        for(int i=0 ;i<=howManyMeasures;i++) {
            weatherMeasuresListOWM.add(i,new WeatherMeasureOWM(dataFromOWM.tempInCity(i), dataFromOWM.windSpeedInCity(i), dataFromOWM.humidityInCity(i), dataFromOWM.pressureInCity(i), owmClaudinesTranslatorRepository.translateEnglishToPolish(dataFromOWM.claudinessInCity(i)), dataFromOWM.dateOfMeasure(i)));
        }
        }

    public ArrayList<WeatherMeasureOWM> getWeatherMeasuresListOWM() {
        return weatherMeasuresListOWM;
    }
}
