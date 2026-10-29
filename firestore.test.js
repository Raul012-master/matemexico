const { initializeTestEnvironment, assertFails, assertSucceeds } = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

const PROJECT_ID = "demo-no-project";
let testEnv;

before(async () => {
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules: fs.readFileSync("firestore.rules", "utf8"),
      host: "127.0.0.1",
      port: 8085,
    },
  });
});

beforeEach(async () => {
  await testEnv.clearFirestore();
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

test("Unauthenticated user cannot read or write user profiles", async () => {
  const unauthedDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthedDb.collection("users").doc("user_123").get());
  await assertFails(unauthedDb.collection("users").doc("user_123").set({
    userId: "user_123",
    displayName: "Mateo",
    grade: "3_primaria",
    selectedState: "Oaxaca",
    characterAvatar: "ajolote",
    totalPoints: 100,
    currentStreak: 1,
    createdAt: new Date(),
    updatedAt: new Date()
  }));
});

test("Authenticated user can create and read their own profile", async () => {
  const aliceDb = testEnv.authenticatedContext("alice_uid").firestore();
  await assertSucceeds(aliceDb.collection("users").doc("alice_uid").set({
    userId: "alice_uid",
    displayName: "Alicia",
    grade: "4_primaria",
    selectedState: "Jalisco",
    characterAvatar: "xolo",
    totalPoints: 250,
    currentStreak: 3,
    createdAt: new Date(),
    updatedAt: new Date()
  }));

  const doc = await assertSucceeds(aliceDb.collection("users").doc("alice_uid").get());
});

test("Authenticated user cannot access or modify another user's profile", async () => {
  const aliceDb = testEnv.authenticatedContext("alice_uid").firestore();
  const bobDb = testEnv.authenticatedContext("bob_uid").firestore();

  // Bob creates his profile
  await assertSucceeds(bobDb.collection("users").doc("bob_uid").set({
    userId: "bob_uid",
    displayName: "Roberto",
    grade: "5_primaria",
    selectedState: "Puebla",
    characterAvatar: "jaguar",
    totalPoints: 50,
    currentStreak: 1,
    createdAt: new Date(),
    updatedAt: new Date()
  }));

  // Alice cannot read Bob's profile
  await assertFails(aliceDb.collection("users").doc("bob_uid").get());

  // Alice cannot overwrite Bob's profile
  await assertFails(aliceDb.collection("users").doc("bob_uid").set({
    userId: "bob_uid",
    displayName: "Hacked",
    grade: "5_primaria",
    selectedState: "Puebla",
    characterAvatar: "jaguar",
    totalPoints: 9999,
    currentStreak: 1,
    createdAt: new Date(),
    updatedAt: new Date()
  }));
});

test("Authenticated user can read state challenges and contribute points", async () => {
  const aliceDb = testEnv.authenticatedContext("alice_uid").firestore();

  // Create state challenge
  await assertSucceeds(aliceDb.collection("state_challenges").doc("oaxaca").set({
    stateId: "oaxaca",
    stateName: "Oaxaca",
    monthlyTargetPoints: 50000,
    currentPoints: 1000,
    participantsCount: 15,
    updatedAt: new Date()
  }));

  // Read state challenge
  await assertSucceeds(aliceDb.collection("state_challenges").doc("oaxaca").get());

  // Update contribution
  await assertSucceeds(aliceDb.collection("state_challenges").doc("oaxaca").update({
    currentPoints: 1100,
    participantsCount: 16,
    updatedAt: new Date()
  }));
});
