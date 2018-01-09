package com.qwert2603.andrlib.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes(value = {"com.qwert2603.andrlib.generator.GenerateLRChanger"})
public class GenerateLRChangerProcessor extends AbstractProcessor {

    private static final String GENERATED_PACKAGE = "com.qwert2603.andrlib.generated";

    private static String createFileText(String cases) {
        return "package " + GENERATED_PACKAGE + "\n" +
                "\n" +
                "import com.qwert2603.andrlib.base.mvi.load_refresh.LRModel\n" +
                "import com.qwert2603.andrlib.base.mvi.load_refresh.LRModelChanger\n" +
                "import com.qwert2603.andrlib.base.mvi.load_refresh.LRViewState\n" +
                "\n" +
                "class LRModelChangerImpl : LRModelChanger {\n" +
                "    @Suppress(\"UNCHECKED_CAST\")\n" +
                "    override fun <VS : LRViewState> changeLRModel(vs: VS, lrModel: LRModel): VS = when (vs) {\n" +
                cases +
                "        else -> null!!\n" +
                "    } as VS\n" +
                "}";
    }

    private static String createCaseText(String className) {
        return "        is " + className + " -> vs.copy(lrModel = lrModel)\n";
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) return true;

        StringBuilder cases = new StringBuilder();
        for (Element element : roundEnv.getElementsAnnotatedWith(GenerateLRChanger.class)) {
            cases.append(createCaseText(((TypeElement) element).getQualifiedName().toString()));
        }
        try {
            String dir = processingEnv.getOptions().get("kapt.kotlin.generated").replace("kaptKotlin", "kapt");
            String fileName = "LRModelChangerImpl.kt";
            Writer writer = new FileWriter(new File(dir, fileName));
            writer.write(createFileText(cases.toString()));
            writer.flush();
            writer.close();
        } catch (IOException throwable) {
            throw new RuntimeException(throwable);
        }

        return true;
    }
}
