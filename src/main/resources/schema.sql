<<<<<<< HEAD
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
    state TEXT NOT NULL,
    date_celebration TEXT NOT NULL,
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
=======
DROP TABLE IF EXISTS SponsorOrganizations;
DROP TABLE IF EXISTS SponsorContacts;
DROP TABLE IF EXISTS GBMembers;
DROP TABLE IF EXISTS Activities;
DROP TABLE IF EXISTS Movements;
DROP TABLE IF EXISTS Levels;
DROP TABLE IF EXISTS ActivityLevel;
DROP TABLE IF EXISTS SponsorshipAgreements;
DROP TABLE IF EXISTS Invoices;
DROP TABLE IF EXISTS SponsorshipPayments;

CREATE TABLE SponsorOrganizations (
    idSponsorOrganization INTEGER PRIMARY KEY AUTOINCREMENT,
    nameSponsorOrganization TEXT NOT NULL,
    typeSponsorOrganization TEXT NOT NULL,
    AddressSponsorOrganization TEXT, -- Can be skipped at first as it is only needed for the invoice
    nifSponsorOrganization TEXT UNIQUE,
    vatSponsorOrganization TEXT UNIQUE
);

CREATE TABLE SponsorContacts (
    idSponsorContact INTEGER PRIMARY KEY AUTOINCREMENT,
    idSponsorOrganization INTEGER NOT NULL,
    nameSponsorContact TEXT NOT NULL,
    emailSponsorContact TEXT UNIQUE NOT NULL,
    phoneSponsorContact TEXT,
    FOREIGN KEY (idSponsorOrganization) REFERENCES SponsorOrganizations(idSponsorOrganization)
);

CREATE TABLE GBMembers (
    idGBMember INTEGER PRIMARY KEY AUTOINCREMENT,
    nameGBMember TEXT NOT NULL,
    roleGBMember TEXT NOT NULL
);

CREATE TABLE Activities (
    idActivity INTEGER PRIMARY KEY AUTOINCREMENT,
    nameActivity TEXT UNIQUE NOT NULL,
    editionActivity TEXT NOT NULL,
    statusActivity TEXT CHECK(statusActivity IN ('registered', 'planned', 'done', 'cancelled', 'done')) NOT NULL,
    dateStartActivity TEXT,
    dateEndActivity TEXT,
    placeActivity TEXT
);

CREATE TABLE Movements (
    idMovement INTEGER PRIMARY KEY AUTOINCREMENT,
    idActivity INTEGER NOT NULL,
    typeMovement TEXT CHECK(typeMovement IN ('income', 'expense')) NOT NULL,
    conceptMovement TEXT NOT NULL,
    amountMovement REAL NOT NULL,
    dateMovement TEXT,
    ReceiptNumber TEXT,
    statusMovement TEXT CHECK(statusMovement IN ('estimated', 'cancelled', 'paid')) NOT NULL,
    FOREIGN KEY (idActivity) REFERENCES Activities(idActivity)
);

CREATE TABLE Levels (
    idLevel INTEGER PRIMARY KEY AUTOINCREMENT,
    nameLevel TEXT NOT NULL,
    feeLevel REAL NOT NULL
);

CREATE TABLE ActivityLevel ( -- Intermediate table
    idActivityLevel INTEGER PRIMARY KEY AUTOINCREMENT,
    idActivity INTEGER NOT NULL,
    idLevel INTEGER NOT NULL,
    FOREIGN KEY (idActivity) REFERENCES Activities(idActivity),
    FOREIGN KEY (idLevel) REFERENCES Levels(idLevel)
);

CREATE TABLE SponsorshipAgreements (
    idSponsorshipAgreement INTEGER PRIMARY KEY AUTOINCREMENT,
    idSponsorContact INTEGER NOT NULL,
    idGBMember INTEGER NOT NULL,
    amountSponsorshipAgreement REAL NOT NULL,
    dateSponsorshipAgreement TEXT NOT NULL,
    statusSponsorshipAgreement TEXT NOT NULL CHECK (statusSponsorshipAgreement IN ('signed', 'closed', 'modified', 'cancelled')),
    FOREIGN KEY (idSponsorContact) REFERENCES SponsorContacts(idSponsorContact),
    FOREIGN KEY (idGBMember) REFERENCES GBMembers(idGBMember)
);

CREATE TABLE Invoices (
    idInvoice INTEGER PRIMARY KEY, -- Serial number ID of the invoice
    idSponsorshipAgreement INTEGER NOT NULL,
    dateIssueInvoice TEXT NOT NULL,
    dateSentInvoice TEXT,
    dateExpirationInvoice TEXT,
    totalAmountInvoice REAL NOT NULL,
    taxRateInvoice REAL NOT NULL, -- To calculate the net and tax amounts from total
    statusInvoice TEXT NOT NULL CHECK (statusInvoice IN ('Draft', 'Issued', 'Paid', 'Rectified', 'Cancelled'))),
    FOREIGN KEY (idSponsorshipAgreement) REFERENCES SponsorshipAgreements(idSponsorshipAgreement)
);

CREATE TABLE SponsorshipPayments (
    idSponsorshipPayment INTEGER PRIMARY KEY AUTOINCREMENT,
    idInvoice INTEGER NOT NULL,
    dateSponsorshipPayment TEXT NOT NULL,
    amountSponsorshipPayment REAL NOT NULL,
    FOREIGN KEY (idInvoice) REFERENCES Invoices(idInvoice)
);
>>>>>>> refs/heads/develop
