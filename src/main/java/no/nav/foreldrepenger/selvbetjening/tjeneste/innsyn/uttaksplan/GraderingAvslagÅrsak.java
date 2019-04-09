package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.uttaksplan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum GraderingAvslagÅrsak {

	  GRADERING_FØR_UKE_7, 
	  FOR_SEN_SØKNAD,
	  MANGLENDE_GRADERINGSAVTALE,
	  AVSLAG_PGA_100_PROSENT_ARBEID;
	
    private static final Logger LOG = LoggerFactory.getLogger(GraderingAvslagÅrsak.class);

	
	  public static GraderingAvslagÅrsak valueSafelyOf(String årsak) {
	        try {
	            return GraderingAvslagÅrsak.valueOf(årsak);
	        } catch (Exception e) {
	            LOG.warn("Ingen enum verdi for {}", årsak);
	            return null;
	        }
	    }
}
