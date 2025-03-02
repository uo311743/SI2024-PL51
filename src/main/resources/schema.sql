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
    addressSponsorOrganization TEXT, -- Can be skipped at first as it is only needed for the invoice
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
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    role TEXT NOT NULL
);

CREATE TABLE Activities (
    idActivity INTEGER PRIMARY KEY AUTOINCREMENT,
    nameActivity TEXT UNIQUE NOT NULL,
    editionActivity TEXT NOT NULL,
    statusActivity TEXT CHECK(statusActivity IN ('registered', 'planned', 'cancelled', 'done')) NOT NULL,
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
    FOREIGN KEY (idGBMember) REFERENCES GBMembers(id)
);

CREATE TABLE Invoices (
    idInvoice INTEGER PRIMARY KEY, -- Serial number ID of the invoice
    idSponsorshipAgreement INTEGER NOT NULL,
    dateIssueInvoice TEXT NOT NULL,
    dateSentInvoice TEXT,
    dateExpirationInvoice TEXT,
    totalAmountInvoice REAL NOT NULL,
    taxRateInvoice REAL NOT NULL, -- To calculate the net and tax amounts from total
    statusInvoice TEXT NOT NULL CHECK (statusInvoice IN ('Draft', 'Issued', 'Paid', 'Rectified', 'Cancelled')),
    FOREIGN KEY (idSponsorshipAgreement) REFERENCES SponsorshipAgreements(idSponsorshipAgreement)
);

CREATE TABLE SponsorshipPayments (
    idSponsorshipPayment INTEGER PRIMARY KEY AUTOINCREMENT,
    idInvoice INTEGER NOT NULL,
    dateSponsorshipPayment TEXT NOT NULL,
    amountSponsorshipPayment REAL NOT NULL,
    FOREIGN KEY (idInvoice) REFERENCES Invoices(idInvoice)
);