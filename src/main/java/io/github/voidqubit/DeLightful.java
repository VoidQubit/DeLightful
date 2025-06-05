package io.github.voidqubit;

import foundry.veil.api.client.render.light.AreaLight;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DeLightful implements ModInitializer {
    public static final String MOD_ID = "delightful";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static final Random random = new Random();

    public static float sandstormDirectionX = random.nextFloat() * 2.0f - 1.0f;
    public static float sandstormDirectionY = random.nextFloat() * 2.0f - 1.0f;
    public static float sandstormDirectionZ = random.nextFloat() * 2.0f - 1.0f;

    private static boolean flashlightEnabled = false;

    private final ArrayList<String> initMessages = new ArrayList<>(List.of(
            "Loading some DeLightful particles...",
            "You know, there is nothing as satisfying as watching ripples on water.",
            "Now with 87% more moisture.",
            "Rain? Check. Ripples? Check. Sanity? Optional.",
            "Simulating wetness... please do not lick the screen.",
            "Drip drip... splash!",
            "Because vanilla rain was just too... dry.",
            "Polishing every ripple by hand...",
            "Calibrating raindrop impact angles.",
            "DeLightful: Now with extra drizzle.",
            "Generating tiny waves that do nothing but look cool.",
            "Unleashing the storm... aesthetically.",
            "Now with 50% more 'plip' and 'plop'.",
            "Fresher than a creeper in a carwash.",
            "If you're reading this, you're already damp.",
            "Making it rain — responsibly.",
            "Certified DeLightfully soggy.",
            "Warning: May cause puddle envy.",
            "Behold... the drip engine.",
            "Infusing your world with droplets of joy.",
            "Loading rain physics from an alternate dimension.",
            "Adding particles to distract from the existential dread.",
            "Proof that even rain can be beautiful.",
            "More ripples than a duck in a jacuzzi.",
            "The mod you never asked for.",
            "Yes, water has particles now. You're welcome.",
            "Initializing the Hydration Engine™.",
            "Soak it in... slowly.",
            "Splash splash, you're about to crash.",
            "Adding puddle personality.",
            "Raindrops enhanced by wizardry and caffeine.",

            // Fireflies
            "Loading fireflies... definitely not tiny glow-in-the-dark demons.",
            "The bugs are glowing, and this time it's intentional.",
            "Fireflies: Nature's flying LEDs.",
            "Warning: Fireflies do not grant XP when squished.",

            // Leaves
            "Letting the leaves fall... gently... majestically... in 4K.",
            "One leaf, two leaf, red leaf, particle grief.",
            "Autumn update: now with 10% more leaf-based sorrow.",

            // Sandstorms
            "Dust in the wind, modded into particles.",
            "Whipping up sandstorms because visibility is overrated.",
            "Sandstorm engine engaged — hold onto your shaders!",
            "Doo-doo-doo DOO-doo... (*plays Darude in background*)",

            // Flashlight
            "Why does this mod have a flashlight? Don’t ask.",
            "Flashlight added after 3 AM panic coding.",
            "Point. Click. See. Regret nothing.",
            "It's not a bug, it's a feature. The flashlight stays.",

            // Developer Pain
            "This mod cost me my year, my only 2 brain cells, and my last bit of hope.",
            "Somewhere along the way, this stopped being fun.",
            "99% particles, 1% tears.",
            "You see falling leaves. I see unresolved bugs.",
            "I fought the renderer. The renderer won.",
            "Debugging this was a spiritual experience. A bad one.",
            "This started as one ripple. Then it spiraled.",
            "Flashbacks to when the particles faced the wrong way. Every time.",
            "If you enjoy this mod, please tell my sleep schedule it was worth it.",
            "Built with love, rage, and 400 lines of 'why isn't this working'.",
            "Tested in rain, cursed in thunder.",
            "Adding a flashlight shouldn't have been this hard.",
            "I'm not crying, you're crying. It’s the particles. They're emotional.",

            // Misc & Chaos
            "Particles are temporary. Pain is eternal.",
            "Rendering particles: mostly legal, occasionally forbidden.",
            "Are you enjoying DeLightful? Please. I have 1 braincell left.",
            "Loading...pain.",
            "Fireflies so real...that they took 2 weeks to add.",
            "Let there be light. And ripples. And particles. And misery.",
            "A sprinkle of delight, a downpour of suffering.",
            "A gentle rain of particle-based madness."
    ));

    private final ArrayList<String> devInitMessages = new ArrayList<>(List.of(
            "Compiling shaders... again... why?",
            "I removed one line of code and everything exploded.",
            "Fun fact: fixing one bug spawned six new ones.",
            "Build #7852(This is real. I counted them. help. please.): working, somehow. I’m scared.",
            "Load time increased by 319ms. Emotionally devastating.",
            "Added flashlight toggle. Accidentally toggles sanity too.",
            "Spent 2 hours aligning one particle. Still off-center.",
            "Particle math now irrational. Like me.",
            "Flashlight offset logic is legally considered witchcraft.",
            "I call shader bugs; visual improvements.",
            "Day 21: I have become the particle system.",
            "I debugged a sandstorm today. I can never go back.",
            "If you're reading this, send snacks.",
            "Rain sound effect now synced with my tears.",
            "Everything worked. Then I hit save.",
            "Particle overflow. Emotionally and technically.",
            "The flashlight now works in dreams and hallucinations.",
            "This is what a breakdown looks like, but sparkly.",
            "I added leaves. Why did I add leaves?",
            "Dev log says: “Don’t trust past me.”",
            "Mod built successfully. By mistake."
    ));

    // 10/10 naming.
    private final ArrayList<String> logReadersTheseOnesAreForYou = new ArrayList<>(List.of(
            "Log mode: VerboseMax™ — you asked for this.",
            "Loading advanced rain metrics... just kidding.",
            "Debug: Ripple state checksum mismatch (not real)",
            "Trace: flashlight.level = OVER9000",
            "ParticleMatrixEngine initialized. It's watching you.",
            "Logger initialized. Prepare for descent.",
            "Tip: logs don't bite. Except this one.",
            "If you scrolled to this, seek help.",
            "Suspicious silence detected in ParticleSystem.class",
            "Logs logged. Logging log log complete.",
            "TIP: Flashlight alignment has a 12% chance to be wrong.",
            "Debug mode enabled. All bets are off.",
            "Tip: remove this mod and touch grass.",
            "This log entry serves no purpose. You read it anyway.",
            "The world isn't flat. But these particles might be.",
            "Nothing is broken. And that’s what’s wrong.",
            "Pain level? 9000+ (please send help)."
    ));

    public static boolean isFlashlightEnabled() {
        return flashlightEnabled;
    }

    public static void setFlashlightEnabled(boolean toggle) {
        flashlightEnabled = toggle;
    }

    @Override
    public void onInitialize() {
        DeLightfulParticles.init();
        DeLightfulSounds.init();

        // Put all message lists into a list of lists. Gottem.
        List<List<String>> allMessageLists = new ArrayList<>(List.of(
                initMessages,
                logReadersTheseOnesAreForYou
        ));

        // I want myself to make myself suffer. Gottem. Again.
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            allMessageLists.add(devInitMessages);
        }

        for (int i = 0; i < 5; i++) {
            List<String> chosenList = allMessageLists.get(random.nextInt(allMessageLists.size()));
            String message = chosenList.get(random.nextInt(chosenList.size()));
            LOGGER.info(message);
        }
    }
}