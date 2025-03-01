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
    idSponsorOrganization INTEGER PRIMARY KEY AUTOINCREMENT,
    nameSponsorOrganization TEXT NOT NULL,
    typeSponsorOrganization TEXT NOT NULL,
    AddressSponsorOrganization TEXT, -- Can be skipped at first as it is only needed for the invice
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
    nameActivity TEXT NOT NULL,
    editionActivity TEXT NOT NULL,
    statusActivity TEXT,
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
    idGBMembers INTEGER NOT NULL,
    amountSponsorshipAgreement REAL NOT NULL,
    dateSponsorshipAgreement TEXT NOT NULL,
    statusSponsorshipAgreement TEXT NOT NULL,
    FOREIGN KEY (idSponsorContact) REFERENCES SponsorContacts(idSponsorContact),
    FOREIGN KEY (idGBMembers) REFERENCES GBMembers(idGBMembers)
);

CREATE TABLE Invoices (
    idInvoice INTEGER PRIMARY KEY, -- Serial number ID of the invoice
    idSponsorshipAgreement INTEGER NOT NULL,
    dateIssueInvoice TEXT NOT NULL,
    dateSentInvoice TEXT,
    dateExpirationInvoice TEXT,
    totalAmountInvoice REAL NOT NULL,
    taxRateInvoice REAL NOT NULL, -- To calculate the net and tax amounts from total
    statusInvoice TEXT NOT NULL,
    FOREIGN KEY (idSponsorshipAgreement) REFERENCES SponsorshipAgreements(idSponsorshipAgreement)
);

CREATE TABLE SponsorshipPayments (
    idSponsorshipPayment INTEGER PRIMARY KEY AUTOINCREMENT,
    idInvoice INTEGER NOT NULL,
    dateSponsorshipPayment TEXT NOT NULL,
    amountSponsorshipPayments REAL NOT NULL,
    FOREIGN KEY (idInvoice) REFERENCES Invoices(idInvoice)
);