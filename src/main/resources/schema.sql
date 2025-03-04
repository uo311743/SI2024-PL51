DROP TABLE IF EXISTS Movements;
DROP TABLE IF EXISTS ActivityLevels;
DROP TABLE IF EXISTS Activities;
DROP TABLE IF EXISTS Levels;
DROP TABLE IF EXISTS SponsorshipPayments;
DROP TABLE IF EXISTS Invoices;
DROP TABLE IF EXISTS SponsorshipAgreements;
DROP TABLE IF EXISTS SponsorContacts;
DROP TABLE IF EXISTS GBMembers;
DROP TABLE IF EXISTS SponsorOrganizations;

PRAGMA foreign_keys = ON;

CREATE TABLE SponsorOrganizations (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    type TEXT NOT NULL CHECK(lower(type) IN ('private corp.', 'public administration')),
    address TEXT,
    nif TEXT,
    vat TEXT
);

CREATE TABLE GBMembers (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    role TEXT UNIQUE NOT NULL
);

CREATE TABLE SponsorContacts (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    idSponsorOrganization INTEGER NOT NULL,
    name TEXT NOT NULL,
    email TEXT NOT NULL,
    phone TEXT,
    FOREIGN KEY (idSponsorOrganization) REFERENCES SponsorOrganizations(id) ON DELETE CASCADE
);

CREATE TABLE SponsorshipAgreements (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    idSponsorContact INTEGER NOT NULL,
    idGBMember INTEGER NOT NULL,
    idActivity TEXT NOT NULL,
    amount REAL NOT NULL,
    date TEXT NOT NULL,
    status TEXT NOT NULL CHECK (lower(status) IN ('signed', 'closed', 'modified', 'cancelled')),
    FOREIGN KEY (idSponsorContact) REFERENCES SponsorContacts(id) ON DELETE CASCADE,
    FOREIGN KEY (idGBMember) REFERENCES GBMembers(id) ON DELETE CASCADE
    FOREIGN KEY (idActivity) REFERENCES Activity(id) ON DELETE CASCADE
);

CREATE TABLE Invoices (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    idSponsorshipAgreement INTEGER NOT NULL,
    dateIssued TEXT NOT NULL,
    dateSent TEXT,
    dateExpiration TEXT,
    totalAmount REAL NOT NULL,
    taxRate REAL NOT NULL,
    status TEXT NOT NULL CHECK (lower(status) IN ('draft', 'issued', 'paid', 'rectified', 'cancelled')),
    FOREIGN KEY (idSponsorshipAgreement) REFERENCES SponsorshipAgreements(id) ON DELETE CASCADE
);

CREATE TABLE SponsorshipPayments (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    idInvoice INTEGER NOT NULL,
    date TEXT NOT NULL,
    amount REAL NOT NULL,
    FOREIGN KEY (idInvoice) REFERENCES Invoices(id) ON DELETE CASCADE
);

CREATE TABLE Activities (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT UNIQUE NOT NULL,
    status TEXT NOT NULL CHECK (lower(status) IN ('registered', 'planned', 'cancelled', 'done')),
    dateStart TEXT,
    dateEnd TEXT,
    place TEXT
);

CREATE TABLE ActivityLevels (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    idActivity INTEGER NOT NULL,
    idLevel INTEGER NOT NULL,
    FOREIGN KEY (idActivity) REFERENCES Activities(id) ON DELETE CASCADE,
    FOREIGN KEY (idLevel) REFERENCES Levels(id) ON DELETE CASCADE
);

CREATE TABLE Levels (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    fee REAL NOT NULL
);

CREATE TABLE Movements (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    idActivity INTEGER NOT NULL,
    type TEXT NOT NULL CHECK (lower(type) IN ('income', 'expense')),
    concept TEXT NOT NULL,
    amount REAL NOT NULL,
    date TEXT,
    receiptNumber TEXT,
    status TEXT NOT NULL CHECK(lower(status) IN ('estimated', 'cancelled', 'paid')),
    FOREIGN KEY (idActivity) REFERENCES Activities(id) ON DELETE CASCADE
);