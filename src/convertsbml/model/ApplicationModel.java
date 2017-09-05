package convertsbml.model;

import convertsbml.model.entities.matlab.ModelMatlab;
import convertsbml.model.entities.slv.ModelSlv;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Główny slvModels aplikacji zawierający dane, przynależny do
 {@link ApplicationController}.
 *
 * @author Magda
 */
public class ApplicationModel {

    private List<ModelSlv> slvModels;
    private ModelMatlab matlabModel;
    private File slvModelFile;

    public ApplicationModel() {
        slvModels = new ArrayList<>();
    }

    public List<ModelSlv> getSlvModels() {
        return slvModels;
    }

    public void addSlvModel(ModelSlv modelSlv) {
        slvModels.add(modelSlv);
    }

    public File getSlvModelFile() {
        return slvModelFile;
    }

    public void setSlvModelFile(File slvModelFile) {
        this.slvModelFile = slvModelFile;
    }

    public ModelMatlab getMatlabModel() {
        return matlabModel;
    }

    public void setMatlabModel(ModelMatlab matlabModel) {
        this.matlabModel = matlabModel;
    }

}
