package no.nav.foreldrepenger.selvbetjening.http;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggReferanse;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.validering.VedlegglistestørrelseConstraint;

class RestApiInputValideringDtoTest extends RestApiTestUtil {


    /**
     * IKKE ignorer eller fjern denne testen, den sørger for at inputvalidering er i orden for REST-grensesnittene
     */
    @ParameterizedTest
    @MethodSource("finnAlleDtoTyper")
    void alle_felter_i_objekter_som_brukes_som_inputDTO_skal_enten_ha_valideringsannotering_eller_være_av_godkjent_type(Class<?> dto) throws ClassNotFoundException {
        Set<Class<?>> validerteKlasser = new HashSet<>(); // trengs for å unngå løkker og unngå å validere samme klasse flere multipliser dobbelt
        validerRekursivt(validerteKlasser, dto, null);
    }

    private static final Set<Class<? extends Object>> ALLOWED_ENUM_ANNOTATIONS = Set.of(JsonProperty.class,
        JsonValue.class,
        JsonIgnore.class,
        Valid.class,
        Null.class,
        NotNull.class);

    @SuppressWarnings("rawtypes")
    private static final Map<Class, List<List<Class<? extends Annotation>>>> UNNTATT_FRA_VALIDERING = new HashMap<>() {
        {
            put(boolean.class, List.of(emptyList()));
            put(Boolean.class, List.of(emptyList()));
            put(byte[].class, List.of(emptyList()));

            // LocalDate og LocalDateTime har egne deserializers
            put(LocalDate.class, List.of(emptyList()));
            put(LocalDateTime.class, List.of(emptyList()));

            // Enforces av UUID og URI selv
            put(UUID.class, List.of(emptyList()));
            put(URI.class, List.of(emptyList()));
            put(VedleggReferanse.class, List.of());
        }
    };

    @SuppressWarnings("rawtypes")
    private static final Map<Class, List<List<Class<? extends Annotation>>>> VALIDERINGSALTERNATIVER = new HashMap<>() {
        {
            put(String.class, List.of(List.of(Pattern.class, Size.class), List.of(Pattern.class), List.of(Digits.class)));
            put(Long.class, List.of(List.of(Min.class, Max.class), List.of(Digits.class)));
            put(long.class, List.of(List.of(Min.class, Max.class), List.of(Digits.class)));
            put(Double.class, List.of(List.of(Min.class, Max.class), List.of(Digits.class)));
            put(double.class, List.of(List.of(Min.class, Max.class), List.of(Digits.class)));
            put(Integer.class, List.of(List.of(Min.class, Max.class), List.of(Digits.class)));
            put(int.class, List.of(List.of(Min.class, Max.class), List.of(Digits.class)));
            put(BigDecimal.class, List.of(List.of(Min.class, Max.class, Digits.class), List.of(DecimalMin.class, DecimalMax.class, Digits.class)));
            put(List.class, List.of(List.of(VedlegglistestørrelseConstraint.class)));


            putAll(UNNTATT_FRA_VALIDERING);
        }
    };

    private static List<List<Class<? extends Annotation>>> getVurderingsalternativer(Field field) {
        var type = field.getType();
        if (field.getType().isEnum()) {
            return Collections.singletonList(Collections.singletonList(Valid.class));
        }
        if (Collection.class.isAssignableFrom(type) || Map.class.isAssignableFrom(type)) {
            if (brukerGenerics(field)) {
                var args = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
                if (Arrays.stream(args).allMatch(UNNTATT_FRA_VALIDERING::containsKey)) {
                    return Collections.singletonList(List.of(Size.class));
                }
            }
            if (harKorrektvedlegglisteConstraint(field)) {
                return List.of(List.of(VedlegglistestørrelseConstraint.class));
            }
            return singletonList(List.of(Valid.class, Size.class));

        }
        return VALIDERINGSALTERNATIVER.get(type);
    }

    private static boolean harKorrektvedlegglisteConstraint(Field field) {
        var aktuell =
            brukerGenerics(field) && field.getType().equals(List.class) && field.getGenericType().getTypeName().contains(VedleggDto.class.getName());
        return aktuell && field.isAnnotationPresent(VedlegglistestørrelseConstraint.class);
    }

    private static Set<Class<?>> finnAlleDtoTyper() {
        Set<Class<?>> parametre = new TreeSet<>(Comparator.comparing(Class::getName));
        for (var method : finnAlleRestMetoder()) {
            parametre.addAll(List.of(method.getParameterTypes()));
            for (var type : method.getGenericParameterTypes()) {
                if (type instanceof ParameterizedType) {
                    var genericTypes = (ParameterizedType) type;
                    for (var gen : genericTypes.getActualTypeArguments()) {
                        parametre.add((Class<?>) gen);
                    }
                }
            }
        }
        return parametre.stream()
            // ikke sjekk nedover i innebygde klasser, det skal brukes annoteringer på tidligere tidspunkt
            .filter(klasse -> !(klasse.getName().startsWith("java") || klasse.getName().startsWith("org.springframework")))
            .collect(Collectors.toSet());
    }

    private static void validerRekursivt(Set<Class<?>> besøkteKlasser, Class<?> klasse, Class<?> forrigeKlasse) throws ClassNotFoundException {
        if (besøkteKlasser.contains(klasse)) {
            return;
        }

        var protectionDomain = klasse.getProtectionDomain();
        var codeSource = protectionDomain.getCodeSource();
        if (codeSource == null) {
            // system klasse
            return;
        }

        besøkteKlasser.add(klasse);
        if (klasse.isInterface()) {
            var subklasser = hentAlleSubclassesAvInterface(klasse);
            for (var subklass : subklasser) {
                validerRekursivt(besøkteKlasser, subklass, forrigeKlasse);
            }
        }


        for (var field : getRelevantFields(klasse)) {
            if (field.getAnnotation(JsonIgnore.class) != null) {
                continue; // feltet blir hverken serialisert elle deserialisert, unntas fra sjekk
            }
            if (field.getType().isEnum()) {
                validerEnum(field);
                continue; // enum er OK
            }
            if (getVurderingsalternativer(field) != null) {
                validerRiktigAnnotert(field); // har konfigurert opp spesifikk validering
            } else if (field.getType().getName().startsWith("java")) {
                throw new AssertionError(
                    "Feltet " + field + " har ikke påkrevde annoteringer. Trenger evt. utvidelse av denne testen for å akseptere denne typen.");
            } else {
                validerHarValidAnnotering(field);
                validerRekursivt(besøkteKlasser, field.getType(), forrigeKlasse);
            }
            if (brukerGenerics(field)) {
                validerRekursivt(besøkteKlasser, field.getType(), forrigeKlasse);
                for (var klazz : genericTypes(field)) {
                    validerRekursivt(besøkteKlasser, klazz, forrigeKlasse);
                }
            }
        }
    }

    private static Set<? extends Class<?>> hentAlleSubclassesAvInterface(Class<?> klasse) throws ClassNotFoundException {
        var provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AssignableTypeFilter(klasse));
        var components = provider.findCandidateComponents("no.nav");
        Set<Class<?>> set = new HashSet<>();
        for (BeanDefinition component : components) {
            Class<?> aClass = Class.forName(component.getBeanClassName());
            set.add(aClass);
        }
        return set;
    }


    private static void validerEnum(Field field) {
        var illegal = Stream.of(field.getAnnotations()).filter(a -> !ALLOWED_ENUM_ANNOTATIONS.contains(a.annotationType())).toList();
        if (!illegal.isEmpty()) {
            throw new AssertionError("Ugyldige annotasjoner funnet på [" + field + "]: " + illegal);
        }

    }

    private static void validerHarValidAnnotering(Field field) {
        if (field.getAnnotation(Valid.class) == null) {
            throw new AssertionError("Feltet " + field + " må ha @Valid-annotering.");
        }
    }

    private static Set<Class<?>> genericTypes(Field field) {
        Set<Class<?>> klasser = new HashSet<>();
        var type = (ParameterizedType) field.getGenericType();
        for (var t : type.getActualTypeArguments()) {
            klasser.add((Class<?>) t);
        }
        return klasser;
    }

    private static boolean brukerGenerics(Field field) {
        return field.getGenericType() instanceof ParameterizedType;
    }

    private static Set<Field> getRelevantFields(Class<?> klasse) {
        Set<Field> fields = new LinkedHashSet<>();
        while (klasse != null && !klasse.isPrimitive() && !(klasse.getName().startsWith("java"))) {
            fields.addAll(fjernStaticFields(List.of(klasse.getDeclaredFields())));
            klasse = klasse.getSuperclass();
        }
        return fields;
    }

    private static Collection<Field> fjernStaticFields(List<Field> fields) {
        return fields.stream().filter(f -> !Modifier.isStatic(f.getModifiers())).collect(Collectors.toList());
    }

    private static void validerRiktigAnnotert(Field field) {
        var alternativer = getVurderingsalternativer(field);
        for (var alternativ : alternativer) {
            var harAlleAnnoteringerForAlternativet = true;
            for (var annotering : alternativ) {
                if (field.getAnnotation(annotering) == null) {
                    harAlleAnnoteringerForAlternativet = false;
                }
            }
            if (harAlleAnnoteringerForAlternativet) {
                return;
            }
        }
        throw new IllegalArgumentException("Feltet " + field + " har ikke påkrevde annoteringer: " + alternativer);
    }
}
