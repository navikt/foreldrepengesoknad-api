package no.nav.foreldrepenger.selvbetjening.http;

import org.reflections.Reflections;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

abstract class RestApiTestUtil {

    static final Function<Method, String> printKlasseOgMetodeNavn = method -> String.format("%s.%s", method.getDeclaringClass(), method.getName());

    private static final Reflections REFLECTION = new Reflections("no.nav.foreldrepenger.selvbetjening");


    static Set<Class<?>> hentAlleRestControllerKlasser() {
        return REFLECTION.getTypesAnnotatedWith(RestController.class).stream()
            .filter(aClass -> !Objects.equals(ProtectedRestController.class, aClass))
            .collect(Collectors.toSet());
    }

    static List<Method> finnAlleRestMetoder() {
        List<Method> restMetodenr = new ArrayList<>();
        for (var klasse : hentAlleRestControllerKlasser()) {

            var metoder = Arrays.stream(klasse.getDeclaredMethods())
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .filter(method -> !method.getName().equalsIgnoreCase("toString"))
                .toList();
            restMetodenr.addAll(metoder);
        }
        return restMetodenr;
    }
}


