package no.nav.foreldrepenger.selvbetjening.vedlegg;

/*
 * @ExtendWith(SpringExtension.class) public class VedleggSjekkerTest {
 * 
 * private static final DataSize MAX_SAMLET = DataSize.of(32, MEGABYTES);
 * private static final DataSize MAX_ENKELT = DataSize.of(8, MEGABYTES);
 * 
 * @Mock VirusScanner scanner;
 * 
 * @Mock PDFEncryptionChecker encryptionSjekker;
 * 
 * private VedleggSjekker sjekker;
 * 
 * @BeforeEach public void beforeAll() { sjekker = new
 * VedleggSjekker(MAX_SAMLET, MAX_ENKELT, scanner, encryptionSjekker); }
 * 
 * @Test public void testSjekkEncrypted() { Vedlegg v =
 * vedleggFra("pdf/pdf-with-user-password.pdf");
 * doThrow(AttachmentPasswordProtectedException.class).when(encryptionSjekker).
 * checkEncrypted(eq(v));
 * assertThrows(AttachmentPasswordProtectedException.class, () ->
 * sjekker.sjekk(v)); verify(scanner).scan(eq(v)); }
 * 
 * @Test public void testSjekkUnencrypted() { Vedlegg v =
 * vedleggFra("pdf/pdf-with-empty-user-password.pdf"); sjekker.sjekk(v);
 * verify(scanner).scan(eq(v)); }
 * 
 * @Test public void testImage() { Vedlegg v = vedleggFra("pdf/nav-logo.png");
 * sjekker.sjekk(v); verify(scanner).scan(eq(v)); }
 * 
 * @Test public void testVirus() { Vedlegg v =
 * vedleggFra("pdf/pdf-with-empty-user-password.pdf");
 * doThrow(AttachmentVirusException.class).when(scanner).scan(eq(v));
 * assertThrows(AttachmentVirusException.class, () -> sjekker.sjekk(v));
 * verify(scanner).scan(eq(v)); }
 * 
 * private static Vedlegg vedleggFra(String navn) { Vedlegg v = new Vedlegg();
 * v.setContent(bytesFra(navn)); v.setUrl(URI.create(navn)); return v; }
 * 
 * private static byte[] bytesFra(String filename) { try (InputStream is = new
 * ClassPathResource(filename).getInputStream()) { return copyToByteArray(is); }
 * catch (IOException e) { throw new IllegalArgumentException(e); } }
 * 
 * }
 */