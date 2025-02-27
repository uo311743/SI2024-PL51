DROP TABLE SponsorOrganizations;
DROP TABLE GBMembers;
DROP TABLE IncomesExpenses;
DROP TABLE SponsorContact;
DROP TABLE SponsorshipAgreements;
DROP TABLE Activities;
DROP TABLE SponsorshipPayments;
DROP TABLE Invoices;
DROP TABLE ActivityLevel;
DROP TABLE Levels;

CREATE TABLE SponsorOrganizations (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    type TEXT CHECK(type IN ('Private corp.', 'Public administration')) NOT NULL,
    invoice_address TEXT,
    nif_vat TEXT UNIQUE NOT NULL
);

CREATE TABLE GBMembers (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    role TEXT NOT NULL
);

CREATE TABLE SponsorContact (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    sponsor_org_id INTEGER NOT NULL,
    name TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    phone TEXT,
    FOREIGN KEY (sponsor_org_id) REFERENCES SponsorOrganizations(id) ON DELETE CASCADE
);

CREATE TABLE SponsorshipAgreements (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    sponsor_org_id INTEGER NOT NULL,
    amount REAL NOT NULL,
    date_agreement TEXT NOT NULL,
    status TEXT CHECK(status IN ('estimated', 'paid', 'cancelled')) NOT NULL,
    FOREIGN KEY (sponsor_org_id) REFERENCES SponsorOrganizations(id) ON DELETE CASCADE
);

CREATE TABLE Invoices (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    sponsorship_agreement_id INTEGER NOT NULL,
    date_issue TEXT NOT NULL,
    date_send TEXT,
    FOREIGN KEY (sponsorship_agreement_id) REFERENCES SponsorshipAgreements(id) ON DELETE CASCADE
);

CREATE TABLE SponsorshipPayments (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    invoice_id INTEGER NOT NULL,
    date_received TEXT NOT NULL,
    amount REAL NOT NULL,
    FOREIGN KEY (invoice_id) REFERENCES Invoices(id) ON DELETE CASCADE
);

CREATE TABLE Activities (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    edition INTEGER NOT NULL,
    state TEXT,
    date_celebration TEXT,
    place_celebration TEXT,
    estimated_income REAL,
    estimated_expenses REAL
);

CREATE TABLE ActivityLevel (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    activity_id INTEGER NOT NULL,
    level_id INTEGER NOT NULL,
    FOREIGN KEY (activity_id) REFERENCES Activities(id) ON DELETE CASCADE,
    FOREIGN KEY (level_id) REFERENCES Levels(id) ON DELETE CASCADE
);

CREATE TABLE Levels (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    fee REAL NOT NULL
);

CREATE TABLE IncomesExpenses (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    concept TEXT NOT NULL,
    amount REAL NOT NULL,
    date_movement TEXT NOT NULL,
    status TEXT CHECK(status IN ('unpaid', 'paid')) NOT NULL
);
