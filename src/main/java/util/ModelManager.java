package util;

import model.*;

public class ModelManager {

    private static volatile ModelManager instance;
    
    private ActivitiesModel activitiesModel;
    private ActivityTemplatesModel activityTemplatesModel;
    private GBMembersModel gbMembersModel;
    private InvoicesModel invoicesModel;
    private LevelsModel levelsModel;
    private MovementsModel movementsModel;
    private SponsorContactsModel sponsorContactsModel;
    private SponsorOrganizationsModel sponsorOrganizationsModel;
    private SponsorshipAgreementsModel sponsorshipAgreementsModel;
    private SponsorshipPaymentsModel sponsorshipPaymentsModel;

    // Private constructor to prevent instantiation
    private ModelManager() {}

    // Double-checked locking for thread safety and performance
    public static ModelManager getInstance() {
        if (instance == null) {
            synchronized (ModelManager.class) {
                if (instance == null) {
                    instance = new ModelManager();
                }
            }
        }
        return instance;
    }

    /**
     * Returns an instance of {@code ActivitiesModel}.
     * Note that lazy initialization is used.
     *
     * @return The {@code ActivitiesModel} instance.
     */
    public ActivitiesModel getActivitiesModel() {
        if (this.activitiesModel == null) {
            this.activitiesModel = new ActivitiesModel();
        }
        return this.activitiesModel;
    }

    /**
     * Returns an instance of {@code ActivityTemplatesModel}.
     * Note that lazy initialization is used.
     *
     * @return The {@code ActivityTemplatesModel} instance.
     */
    public ActivityTemplatesModel getActivityTemplatesModel() {
        if (this.activityTemplatesModel == null) {
            this.activityTemplatesModel = new ActivityTemplatesModel();
        }
        return this.activityTemplatesModel;
    }

    /**
     * Returns an instance of {@code GBMembersModel}.
     * Note that lazy initialization is used.
     *
     * @return The {@code GBMembersModel} instance.
     */
    public GBMembersModel getGBMembersModel() {
        if (this.gbMembersModel == null) {
            this.gbMembersModel = new GBMembersModel();
        }
        return this.gbMembersModel;
    }

    /**
     * Returns an instance of {@code InvoicesModel}.
     * Note that lazy initialization is used.
     *
     * @return The {@code InvoicesModel} instance.
     */
    public InvoicesModel getInvoicesModel() {
        if (this.invoicesModel == null) {
            this.invoicesModel = new InvoicesModel();
        }
        return this.invoicesModel;
    }

    /**
     * Returns an instance of {@code LevelsModel}.
     * Note that lazy initialization is used.
     *
     * @return The {@code LevelsModel} instance.
     */
    public LevelsModel getLevelsModel() {
        if (this.levelsModel == null) {
            this.levelsModel = new LevelsModel();
        }
        return this.levelsModel;
    }

    /**
     * Returns an instance of {@code MovementsModel}.
     * Note that lazy initialization is used.
     *
     * @return The {@code MovementsModel} instance.
     */
    public MovementsModel getMovementsModel() {
        if (this.movementsModel == null) {
            this.movementsModel = new MovementsModel();
        }
        return this.movementsModel;
    }

    /**
     * Returns an instance of {@code SponsorContactsModel}.
     * Note that lazy initialization is used.
     *
     * @return The {@code SponsorContactsModel} instance.
     */
    public SponsorContactsModel getSponsorContactsModel() {
        if (this.sponsorContactsModel == null) {
            this.sponsorContactsModel = new SponsorContactsModel();
        }
        return this.sponsorContactsModel;
    }

    /**
     * Returns an instance of {@code SponsorOrganizationsModel}.
     * Note that lazy initialization is used.
     *
     * @return The {@code SponsorOrganizationsModel} instance.
     */
    public SponsorOrganizationsModel getSponsorOrganizationsModel() {
        if (this.sponsorOrganizationsModel == null) {
            this.sponsorOrganizationsModel = new SponsorOrganizationsModel();
        }
        return this.sponsorOrganizationsModel;
    }

    /**
     * Returns an instance of {@code SponsorshipAgreementsModel}.
     * Note that lazy initialization is used.
     *
     * @return The {@code SponsorshipAgreementsModel} instance.
     */
    public SponsorshipAgreementsModel getSponsorshipAgreementsModel() {
        if (this.sponsorshipAgreementsModel == null) {
            this.sponsorshipAgreementsModel = new SponsorshipAgreementsModel();
        }
        return this.sponsorshipAgreementsModel;
    }

    /**
     * Returns an instance of {@code SponsorshipPaymentsModel}.
     * Note that lazy initialization is used.
     *
     * @return The {@code SponsorshipPaymentsModel} instance.
     */
    public SponsorshipPaymentsModel getSponsorshipPaymentsModel() {
        if (this.sponsorshipPaymentsModel == null) {
            this.sponsorshipPaymentsModel = new SponsorshipPaymentsModel();
        }
        return this.sponsorshipPaymentsModel;
    }
}

